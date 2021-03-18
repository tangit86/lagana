import React from 'react';
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import { Container, Row, Col } from 'react-bootstrap'
import { FormView } from './FormView'



export default class View extends React.Component {


    constructor(props) {
        super(props);
        this.save = this.save.bind(this);
        this.change = this.change.bind(this);
        this.clone = this.clone.bind(this);
        this.delete = this.delete.bind(this);
    }

   change(id,key,value){
       this.props.onChange(this.props.id,id,key,value);
   }

    save(obj) {
        console.log("on save called....",this.props.title,obj)
    }

    delete() {

    }

    clone() {

    }

    render() {
        const tProps = this.props;
        return (
            <Container fluid>
                <Row>
                    <Col style={{ width: "100%" }}>
                        <h1>{tProps.title}</h1>
                        <BootstrapTable data={tProps.rows} expandComponent={(row) => getExpandComponent(row,tProps,this.change,this.save)} expandableRow={() => { return true }}>
                            {
                                tProps.cols.map(col => {
                                    if (col.isHead) {
                                        return [<TableHeaderColumn
                                            isKey={col.isKey}
                                            width={col.isKey ? 500 : undefined}
                                            dataField={col.id}
                                            dataFormat={col.formatter ? (cell, row) => col.formatter(cell) : undefined}
                                            dataSort={col.isSortable}
                                            sorta
                                        >{col.id}</TableHeaderColumn>]
                                    }
                                })
                            }
                            <TableHeaderColumn width="200px" dataFormat={(cell, row, onClone, onDelete) => actionFormatter(cell, row, this.clone, this.delete)}></TableHeaderColumn>
                        </BootstrapTable>
                    </Col>
                </Row>
            </Container>
        )
    }
}


function getExpandComponent(row,tProps,onChange,onSave) {    
    const formViewProps = {
        row: row,
        cols: tProps.cols,
        onChange: onChange,
        onSave:onSave
    }
    return (
        formViewProps.cols ? <FormView {...formViewProps} /> : null
    )
}

function actionFormatter(cell, row, onClone, onDelete) {
    return (
        <div>
            <a href="#" onClick={(e) => onClone(row)}>Clone</a> | <a href="#" onClick={(e) => onDelete(row)}>Delete</a> <span title={"Total uses of element in the schema: " + row.count}>({row.count})</span>
        </div>
    )
}




class SelectorEditor extends React.Component {

    constructor(props) {
        super(props)

        console.log("multi props", props)
        this.state = {
            selected: this.props.defaultValue
        }
    }


    onChange(e) {
        console.log(e);
        this.state.selected.push(e.target.id);
    }

    render() {
        const dataSource = [];
        if (this.props.dataSource) {
            dataSource = this.props.dataSource;
        }
        return (
            <select name={1} id="multiSelector" multiple={true} onChange={this.onChange}>
                {
                    dataSource.map(m => {
                        const isSelected = this.state.selected.filter(f => f.name === m.name);
                        return [<option selected={isSelected} value={m.name}>{m.name}</option>]
                    })
                }
            </select>
        )
    }
}

