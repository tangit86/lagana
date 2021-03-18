import React from 'react';
import ReactTable from 'react-table-v6'
import 'react-table-v6/react-table.css'
import Button from 'react-bootstrap/Button'
import { timeConverter } from '../formatters/formatters'
import { ApiControllerApiFactory, IFeederOverview } from '../api/gen/index';
import { Container } from 'react-bootstrap';

export default class Search extends React.Component {

    apiClient = null;

    constructor(props){
        super(props)
        this.state = {
             isLive:false,
             allFields:this.props.allFields,
             initTimer:5000,
             timer:3000
        }

        this.apiClient = new ApiControllerApiFactory();
        this.toggleLive = this.toggleLive.bind(this)
        this.poll = this.poll.bind(this)
    }

    pollVar=null

    toggleLive(){
        const nextState =  !this.state.isLive
        this.setState({ isLive: nextState, timer:this.state.initTimer })
        this.pollVar = nextState ? setInterval(this.poll,1000) : clearInterval(this.pollVar);
    }

    poll(){
        var updTimer = this.state.timer;
        if(updTimer==0){
            this.props.fetch()
            updTimer = this.state.initTimer
        }else{
            updTimer-=1000;
        }

        this.setState({timer:updTimer})
    }

    render(){
        console.log(this.pollVar,"pollVar")
        return (<form>
            <Button variant="info" onClick={this.props.fetch}>Search</Button>
            &nbsp;
            <label class="switch">
                            <input type="checkbox" value={this.state.isLive} onChange={this.toggleLive} />
                            <span class="slider round"></span>
                        </label>
                        <span>{this.state.isLive ? `Reload in ${this.state.timer/1000}s` : ""}</span>
          </form>)
    }
}




function getFilterView(filter){

}

function getFilterEdit(filter){

}