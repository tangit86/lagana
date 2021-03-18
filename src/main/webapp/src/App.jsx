import React from 'react';
import {Tabs,Tab,TabPanel,TabList} from 'react-tabs';
import './App.css';
import './xmlFormatter.css';
import 'react-tabs/style/react-tabs.css';
import Logview from './views/Logview';
import FeederView from './views/FeederView';
import DashboardView from './views/DashboardView';
import SchemaConfView from './views/schema/SchemaConfView';
import 'bootstrap/dist/css/bootstrap.min.css';


function App(){

    return ( <Tabs>
      <TabList>
        <Tab>Logs</Tab>
        <Tab>Dashboards</Tab>
        <Tab>Feeder Status</Tab>
        <Tab>Configuration</Tab>
      </TabList>
      <TabPanel forceRender><Logview/></TabPanel>
      <TabPanel><DashboardView/></TabPanel>
      <TabPanel><FeederView/></TabPanel>
      <TabPanel><SchemaConfView/></TabPanel>
    </Tabs>);
}

export default App;
