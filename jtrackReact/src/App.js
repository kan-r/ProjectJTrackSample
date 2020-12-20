import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import './App.css';
import AuthService from './service/AuthService';
import AuthenticatedRoute from './component/AuthenticatedRoute';
import Navmenu from './component/Nav';
import Login from './component/Login';
import Job from './component/Job';
import JobCreate from './component/JobCreate';
import JobEdit from './component/JobEdit';
import JobPriority from './component/JobPriority';
import JobPriorityCreate from './component/JobPriorityCreate';
import JobPriorityEdit from './component/JobPriorityEdit';
import JobResolution from './component/JobResolution';
import JobResolutionCreate from './component/JobResolutionCreate';
import JobResolutionEdit from './component/JobResolutionEdit';
import JobStage from './component/JobStage';
import JobStageCreate from './component/JobStageCreate';
import JobStageEdit from './component/JobStageEdit';
import JobStatus from './component/JobStatus';
import JobStatusCreate from './component/JobStatusCreate';
import JobStatusEdit from './component/JobStatusEdit';
import JobType from './component/JobType';
import JobTypeCreate from './component/JobTypeCreate';
import JobTypeEdit from './component/JobTypeEdit';
import Timesheet from './component/Timesheet';
import TimesheetCreate from './component/TimesheetCreate';
import TimesheetEdit from './component/TimesheetEdit';
import TimesheetCode from './component/TimesheetCode';
import TimesheetCodeCreate from './component/TimesheetCodeCreate';
import TimesheetCodeEdit from './component/TimesheetCodeEdit';
import User from './component/User';
import UserCreate from './component/UserCreate';
import UserEdit from './component/UserEdit';

class App extends Component {

  constructor(props) {
    super(props)
    this.state = {
        appUser: ''
    }
    this.updateAppUser = this.updateAppUser.bind(this);
  }

  componentDidMount() {
    this.updateAppUser(AuthService.getAppUser());
  }

  shouldComponentUpdate(nextProps, nextState) {
    if (this.state.appUser !== nextState.appUser) {
      return true;
    }
    return false;
  }

  updateAppUser(user){
    this.setState({appUser:user});
  } 

  render() {
    return (
      <Router basename={`${process.env.PUBLIC_URL}`}>
        <div className="App">
          <Navmenu updateAppUser = {this.updateAppUser} appUser={this.state.appUser} />
          <div>
            <Switch>
              <Route 
                path="/" exact 
                render={props => <Login updateAppUser = {this.updateAppUser} />} 
              />
              <Route 
                path="/Login" exact 
                render={props => <Login updateAppUser = {this.updateAppUser} />} 
              />
              <AuthenticatedRoute path="/Job" exact component={Job} />
              <AuthenticatedRoute path="/JobCreate" exact component={JobCreate} />
              <AuthenticatedRoute path="/JobEdit/:jobNo" exact component={JobEdit} />
              <AuthenticatedRoute path="/JobPriority" exact component={JobPriority} />
              <AuthenticatedRoute path="/JobPriorityCreate" exact component={JobPriorityCreate} />
              <AuthenticatedRoute path="/JobPriorityEdit/:jobPriority" exact component={JobPriorityEdit} />
              <AuthenticatedRoute path="/JobResolution" exact component={JobResolution} />
              <AuthenticatedRoute path="/JobResolutionCreate" exact component={JobResolutionCreate} />
              <AuthenticatedRoute path="/JobResolutionEdit/:jobResolution" exact component={JobResolutionEdit} />
              <AuthenticatedRoute path="/JobStage" exact component={JobStage} />
              <AuthenticatedRoute path="/JobStageCreate" exact component={JobStageCreate} />
              <AuthenticatedRoute path="/JobStageEdit/:jobStage" exact component={JobStageEdit} />
              <AuthenticatedRoute path="/JobStatus" exact component={JobStatus} />
              <AuthenticatedRoute path="/JobStatusCreate" exact component={JobStatusCreate} />
              <AuthenticatedRoute path="/JobStatusEdit/:jobStatus" exact component={JobStatusEdit} />
              <AuthenticatedRoute path="/JobType" exact component={JobType} />
              <AuthenticatedRoute path="/JobTypeCreate" exact component={JobTypeCreate} />
              <AuthenticatedRoute path="/JobTypeEdit/:jobType" exact component={JobTypeEdit} />
              <AuthenticatedRoute path="/Timesheet" exact component={Timesheet} />
              <AuthenticatedRoute path="/TimesheetCreate" exact component={TimesheetCreate} />
              <AuthenticatedRoute path="/TimesheetEdit/:timesheetId" exact component={TimesheetEdit} />
              <AuthenticatedRoute path="/TimesheetCode" exact component={TimesheetCode} />
              <AuthenticatedRoute path="/TimesheetCodeCreate" exact component={TimesheetCodeCreate} />
              <AuthenticatedRoute path="/TimesheetCodeEdit/:timesheetCode" exact component={TimesheetCodeEdit} />
              <AuthenticatedRoute path="/User" exact component={User} />
              <AuthenticatedRoute path="/UserCreate" exact component={UserCreate} />
              <AuthenticatedRoute path="/UserEdit/:userId" exact component={UserEdit} />
            </Switch>
          </div>
        </div>
      </Router>
    );
  }
}

export default App;
