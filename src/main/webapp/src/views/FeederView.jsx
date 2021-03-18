import React from 'react';
import ReactTable from 'react-table-v6'
import 'react-table-v6/react-table.css'
import { ApiControllerApiFactory, IFeederOverview } from '../api/gen/index';
import Button from 'react-bootstrap/Button'

export default class FeederView extends React.Component {

    apiClient = null;

    constructor(props) {
        super(props);

        this.state = {
            feeders: []
        }

        this.getFeeders = this.getFeeders.bind(this);
        this.mapFeeder = this.mapFeeder.bind(this);
        this.start = this.start.bind(this);
        this.stop = this.stop.bind(this);
        this.unregister = this.unregister.bind(this);
        this.clear = this.clear.bind(this);
        this.apiClient = new ApiControllerApiFactory();
    }

    componentDidMount() {
        this.pollVar = setInterval(this.getFeeders, 1000);
    }


    componentWillUnmount() {
        clearInterval(this.pollVar);
        this.pollVar = null;
    }

    getFeeders() {
        this.apiClient.getOverview("").then(
            (response) => {
                const data = response.data;
                const updFeeders = Object.keys(data).map(key => {
                    return this.mapFeeder(data[key]);
                });
                this.setState({ feeders: updFeeders });
            })
    }

    mapFeeder(apiFeeder) {
        return {
            name: apiFeeder.name,
            targetSeries: apiFeeder.targetSeries,
            feederState: apiFeeder.feederState
        }
    }

    start(e) {
        this.apiClient.start(e).then((data) => {

        }, (error) => {
            console.log("ERROR", error);
        });
    }

    stop(e) {
        this.apiClient.stop(e).then((data) => {

        }, (error) => {
            console.log("ERROR", error);
        });
    }

    unregister(e){
        this.apiClient.unregister(e).then((data) => {

        }, (error) => {
            console.log("ERROR", error);
        });
    }

    clear(e){
        this.apiClient.clear(e).then((data) => {

        }, (error) => {
            console.log("ERROR", error);
        });
    }

    render() {

        const columns = [{
            id: "name",
            Header: 'Name (ID)',
            accessor: 'name'
        }, {
            id: "targetSeries",
            Header: 'Target Series',
            accessor: "targetSeries"
        }, {
            id: 'feederState',
            Header: 'Status',
            accessor: 'feederState'
        },
        {
            id: 'action',
            accessor: 'feederState',
            Cell: (props) => {
                if (props.value === 'STOPPED') {
                    return [<Button key="startBtn" variant="success" onClick={e => this.start(props.row.name)}>Start</Button>,
                    <Button key="clearBtn" variant="danger" onClick={e => this.clear(props.row.name)}>Clear</Button>,
                    <Button key="unregBtn" variant="warning" onClick={e => this.unregister(props.row.name)}>Unregister</Button>]
                }else{
                    return (<Button id="stopBtn" variant="danger" onClick={e => this.stop(props.row.name)}>Stop</Button>)
                }
            }
        }
        ]

        return <ReactTable
            showPagination={false}
            data={this.state.feeders}
            columns={columns}
        />
    }
}
