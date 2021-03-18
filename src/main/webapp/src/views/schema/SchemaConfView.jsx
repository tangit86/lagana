import React from 'react';
//import {apiCall, getConfOveriview, saveConfOverview } from '../api/client';
import Button from 'react-bootstrap/Button'
import { ApiControllerApiFactory, FeederSchema } from '../../api/gen/index';
import { Tabs, Tab, TabPanel, TabList } from 'react-tabs';
import 'react-bootstrap-table/dist/react-bootstrap-table-all.min.css';
import { Container, Row, Col } from 'react-bootstrap'
import View from './View'

export default class SchemaConfView extends React.Component {

    laganaApi = null;

    constructor(props) {
        super(props);

        this.state = {
            changed: false,
            feeders: {},
            readers: {},
            fields: {},
            matchers: {},
            uses: {}
        };

        this.getSchema = this.getSchema.bind(this);
        this.saveItem = this.saveItem.bind(this);
        this.change = this.change.bind(this);
        this.saveSchema = this.saveSchema.bind(this);
        this.deleteItem = this.deleteItem.bind(this);
        this.laganaApi = new ApiControllerApiFactory();
    }


    componentDidMount() {
        this.getSchema();
    }

    async getSchema() {
        this.laganaApi.get().then(
            (data) => {
                const payload = data.data.reapSchema;
                let updState = {
                    changed: false,
                    feeders: {},
                    readers: {},
                    fields: {},
                    matchers: {},
                    uses: {}
                }
                Object.keys(payload).forEach(key => {
                    const items = payload[key];
                    items.forEach(item => {
                        updState[key][item.name] = item;
                        const useKey = key + "_" + item.name;
                        if (!updState['uses'][useKey]) {
                            updState['uses'][useKey] = 0;
                        }
                        updState['uses'][useKey] += 1;
                    })
                });
                console.log("updState", updState)
                this.setState(updState)
            });
    }

    saveSchema() {
        this.laganaApi.setConf({
            feeders: this.state.feeders,
            readers: this.state.readers,
            fields: this.state.fields,
            matchers: this.state.matchers
        })
    }

    saveItem(type, key, obj) {
        console.log("saveItem ", type, key, obj)
        const updState = Object.assign({}, this.state);
        const existing = updState[type]
        existing[key] = obj;
        updState["changed"] = true;
        console.log(updState, " UPD STATE")
        this.setState(updState)
    }

    deleteItem(type, key) {
        console.log("going to delete>", type, key)
    }

    change(objGroup, objKey, fieldKey, objVal) {
        console.log("CHANGE: ", objGroup, objKey, fieldKey, objVal)
        let updState = Object.assign({}, this.state[objGroup])
        let item = Object.assign({}, updState[objKey])
        item[fieldKey] = objVal;
        updState[objKey] = item;
        this.setState({
            "changed": true,
            updState
        });
    }

    render() {
        return (
            <Container fluid>
                <Tabs>
                    <TabList>
                        <Tab>Feeders</Tab>
                        <Tab>Matchers</Tab>
                        <Tab>Readers</Tab>
                        <Tab>Fields</Tab>
                    </TabList>
                    <TabPanel forceRender={true}>{getFeeders(this.props, this.state.feeders,this.state.readers,this.state.matchers, this.change, this.saveItem, this.deleteItem)}</TabPanel>
                    <TabPanel forceRender={true}>{getMatchers(this.props, this.state.matchers, this.state.matchers, this.change, this.saveItem, this.deleteItem)}</TabPanel>
                    <TabPanel>ok</TabPanel>
                    <TabPanel>ok</TabPanel>
                </Tabs>
                <Row>
                    <Col></Col>
                    <Col>
                        <Button variant="success" onClick={this.saveSchema} disabled={!this.state.changed}>Save</Button>
                    </Col>
                    <Col></Col>
                </Row>

            </Container>

        )
    }
}


function getMatchers(props, matchers, allMatchers, onChange, onSave, onDelete) {

    const tProps = {
        id: "matchers",
        title: "Matchers",
        key: "name",
        cols: [
            {
                id: "name",
                isKey: true,
                isHead: true,
                isSortable:true
            },
            {
                id: "description",
                isHead: true
            },
            {
                id: "field",
                isHead: true
            },
            {
                id: "regex"
            },
            {
                id: "formatMatchValue"
            },
            {
                id: "defaultMatchValue"
            }, {
                id: "multilineAndInvert"
            },
            {
                id: "allOf",
                formatter: (obj) => { return multiFormatter(obj,allMatchers,true,onChange) },
                editor: (row) => { },
                isHead: true
            },
            {
                id: "anyOf",
                formatter: (obj) => { return multiFormatter(obj,allMatchers,true,onChange) },
                editor: (row) => { },
            }
        ],
        rows: Object.keys(matchers).map(f => { return matchers[f] }),
        onSave: onSave,
        onChange: onChange,
        onDelete: onDelete
    }
    return (<View {...tProps} />)
}

function getFeeders(props,feeders, matchers,readers, onChange, onSave, onDelete) {

    const matchersDs = Object.keys(matchers).map(f => { return matchers[f].name });
    const readersDs = Object.keys(readers).map(f => { return readers[f].name });
    const tProps = {
        id: "feeders",
        title: "Feeders",
        key: "name",
        cols: [
            {
                id: "name",
                isKey: true,
                isHead: true,
                isSortable:true
            },
            {
                id: "description",
                isHead: true
            },
            {
                id: "targetSystem",
                isHead: true
            },
            {
                id: "isRegistered",
                isHead: true
            },
            {
                id: "reader",
                // formatter: (obj) => { return multiFormatter(obj) },
                editor: (cell,onChange) => { return multiEditor(cell,readersDs,false,onChange)},
                isHead: true
            },
            {
                id: "matcher",
                // formatter: (obj) => { return multiFormatter(obj) },
                editor: (cell,onChange) => { return multiEditor(cell,matchersDs,false,onChange)},
                isHead:true
            }
        ],
        rows: Object.keys(feeders).map(f => { return feeders[f] }),
        onSave: onSave,
        onChange: onChange,
        onDelete: onDelete
    }
    return (<View {...tProps} />)
}

function multiFormatter(obj) {
    return (<ul>

        {
            obj.map(t => {
                return [<li><span class="tag" background="red">{t}</span></li>]
            })
        }

    </ul>)
}


function multiEditor(obj,dataSource,isMulti,onChange){
    return (
        <select>
            {
                dataSource.map(o=>{
                return [<option value={o}>{o}</option>]
                })
            }
        </select>
    )
}