import React from 'react';
import ReactTable from 'react-table-v6'
import 'react-table-v6/react-table.css'
import Button from 'react-bootstrap/Button'
import { ApiControllerApiFactory, IFeederOverview } from '../api/gen/index';
import Search from './Search';
import { Container, Row, Col } from 'react-bootstrap';
import { timeFormatter, xmlFormatter } from '../formatters/formatters'
// import DataGrid from 'react-data-grid';
// import 'react-data-grid/dist/react-data-grid.css';


export default class Logview extends React.Component {

  apiClient = null;

  constructor(props) {
    super(props);

    this.state = {
      logs: [],
      search: {
        page: 1,
        pageSize: 100,
        filters: []
      },
      selectedIndex: null,
      selectedRow: null,
      loading: false,
      allFields: []
    }

    this.apiClient = new ApiControllerApiFactory();
    this.fetch = this.fetch.bind(this);
    this.myRef = React.createRef();
  }

  componentDidMount() {
    this.apiClient.fetchSchema().then(
      (response) => {
        const payload = response.data;

        let updState = payload.map(f => f);
        this.setState({ allFields: updState })
      },
      (error) => {
        console.log("ERROR fetching schema", error)
      });
  }


  componentWillUnmount() {

  }

  fetch() {
    this.setState({ loading: true })
    this.apiClient.fetch({
      page: this.state.search.page,
      pageSize: this.state.search.pageSize,
      filters: this.state.search.filters
    }).then(
      (data) => {
        if (!data.data.payload) {
          this.setState({ loading: false })
          return
        }
        this.setState({
          logs: mapLogs(data.data.payload.results),
          loading: false
        }, () => {
          //tODO: use proper dimensions and dynamically
          console.log(this.state.logs.length,"LOG LEN")
          document.getElementsByClassName("rt-tbody").item(0).scrollTop = this.state.logs.length > 15 ? this.state.logs.length * 30 : 0;
        })
      }, (error) => {
        this.setState({ loading: false })
      }
    )
  }

  render() {

    const columns = [{
      id: "@timestamp",
      Header: 'Timestamp',
      accessor: '@timestamp',
      maxWidth: 150,
      Cell: (props) => {return getFieldFormatter("@timestamp", this.state.allFields)(props.row["@timestamp"])}
    },
    {
      id: "@source",
      Header: 'Source',
      accessor: "@source",
      maxWidth: 100
    }, {
      id: "@content",
      Header: 'Content',
      accessor: "@content"
    }
    ]


    const searchProps = {
      fetch: this.fetch,
      state: this.state,
      allFields: this.state.allFields
    }



    const logViewMinWidth = this.state.selectedIndex ? '70%' : '100%';
    return (
      <Container fluid>
        <Row style={{ backgroundColor: 'silver' }}>
          <Col style={{ minWidth: '80%' }}>
            &nbsp;
          </Col>
          <Col className="justify-content-right align-items-right">

            <Search {...searchProps} />

          </Col>
        </Row>

        <Row>
          <Col id="mainCol" style={{ minWidth: { logViewMinWidth } }}>
            <ReactTable
              sortable={false}
              showPagination={false}
              pageSize={100}
              data={this.state.logs}
              columns={columns}
              loading={this.state.loading}
              style={
                { height: window.innerHeight }
              }
              className="-striped -highlight"
              getTbodyProps={(A, B, C, D, E, F) => {
                return {
                  onScroll: (a, b, c, d, e, f) => {
                    console.log("A", A, B, C, D, E, F)
                    console.log("a", a, b, c, d, e, f)
                  }
                }
              }}
              getTrGroupProps={(state, rowInfo, column, instance) => {
                if (rowInfo !== undefined) {
                  const rowKey = rowInfo.original['@timestamp'] + rowInfo.original['@source']
                  return {
                    onClick: (e, handleOriginal) => {
                      console.log(instance)
                      if (this.state.selectedIndex === rowKey) {
                        this.setState({
                          selectedIndex: null,
                          selectedRow: null
                        })
                      }
                      else {
                        this.setState({
                          selectedIndex: rowKey,
                          selectedRow: rowInfo.original
                        })
                      }
                    },
                    style: {
                      cursor: 'pointer',
                      background: rowKey === this.state.selectedIndex ? '#00afec' : 'white',
                      color: rowKey === this.state.selectedIndex ? 'white' : 'black'
                    }
                  }
                }
              }
              }
            />
          </Col>
          <Col id="infoCol" hidden={!this.state.selectedIndex}>
            {
              this.state.selectedRow != null ? getRowDetails(this.state.selectedRow, this.state.allFields) : null
            }
          </Col>
        </Row>

      </Container>)
  }
}

function getRowDetails(row, allFields) {
  return (<table border="5px">
    {
      Object.keys(row).map(key => {
        const formatter = getFieldFormatter(key, allFields);
        let val = formatter ? formatter(row[key]) : row[key]
        console.log("formatter for field",formatter,key)
        return [<tr><td>{key}</td><td width="100%" height="100%">{val}</td></tr>]
      })
    }
  </table>)
}

function getFieldFormatter(fieldKey, allFields) {
  const filter = allFields.filter(f => f.name === fieldKey);

  if (filter.length > 0) {
    const fType = filter[0].type;

    if (fType === "TIMESTAMP") {
      return timeFormatter
    }

    if (fType === "XML") {
      return xmlFormatter
    }
  }

  return (x)=>{return x}
}

function getFetchRequestFromState(state) {
  return {
    "page": 1,
    "pageSize": 10,
    "select": {},
    "filters": [
      {
        "fieldName": "@timestamp",
        "op": "GT",
        "value": "1234562134456"
      }
    ]
  }
}

function mapLogs(results) {

  const logs = Object.keys(results).map(k => {
    const values = results[k].values;
    const log = {};

    Object.keys(values).forEach(valkey => {
      const v = values[valkey];
      Object.keys(v).forEach(kv => {
        const logObj = v[kv];
        log[logObj.fn] = logObj.vl;
      })
    });
    return log;
  });
  return logs;
}