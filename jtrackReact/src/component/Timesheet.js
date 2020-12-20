import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import GenService from '../service/GenService';
import TimesheetService from '../service/TimesheetService';
import UserService from '../service/UserService';
import Message from './Message';

class Timesheet extends Component {

    constructor(props) {
        super(props)
        this.state = {
            timesheetList: [],
            userList: [],
            timesheetSO: {
                userId: '',
                workedDateFrom: '',
                workedDateTo: ''
            },
            msgObj: {
              msg: '',
              isError: true
            }
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        this.getTimesheetList();
        this.getUserList();
    }

    handleSubmit(event) {
        event.preventDefault();
        this.getTimesheetList2(this.state.timesheetSO);
    }

    handleChange(event) {

        let timesheetSO = {
            ...this.state.timesheetSO,
            [event.target.name] : event.target.value
        };

        this.setState({timesheetSO:timesheetSO});
    }

    getTimesheetList(){
        TimesheetService.getTimesheetList()
        .then(res => {
            this.updateMessage('');
            this.setState({ timesheetList: res.data });
        })
        .catch(err => {
            this.updateMessage(err, true);
        });;
    }

    getTimesheetList2(timesheetSO){
        TimesheetService.getTimesheetList2(timesheetSO)
        .then(res => {
            this.updateMessage('');
            this.setState({ timesheetList: res.data });
        })
        .catch(err => {
            this.updateMessage(err, true);
        });;
    }

    deleteTimesheet(timesheetId){
        TimesheetService.deleteTimesheet(timesheetId)
        .then(res => {
            this.getTimesheetList();
        })
        .catch(err => {
            this.updateMessage(err, true);
        });
    }

    getUserList(){
        UserService.getUserList()
        .then(res => {
            this.updateMessage('');
            this.setState({ userList: res.data })
        })
        .catch(err => {
            this.updateMessage(err, true);
        });
    }

    updateMessage(msg, isError = false){
        this.setState({msgObj: {msg:msg, isError:isError}});
    }

    formatDateTime(dtTime){
        return GenService.formatDateTime(dtTime);
    }

    formatDate(dtTime){
        return GenService.formatDate(dtTime);
    }

    formatDay(dtTime){
        return GenService.formatDay(dtTime);
    }

    render(){
        return (
            <div>
                <Message msgObj={this.state.msgObj} />
                <div className="form-region">
                    <form onSubmit={this.handleSubmit}>
                        <table cellPadding="0" border="0" cellSpacing="0" summary="" className="form-standard">
                            <tbody>
                                <tr>
                                    <td>
                                        <table border="0" summary="" >
                                            <tbody>
                                            <tr>
                                                <td nowrap="nowrap" align="right">
                                                    <label>User</label>
                                                </td>
                                                <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                    <select name="userId" value = {this.state.timesheetSO.userId} onChange={this.handleChange}>
                                                        <option value=""></option>
                                                        {this.state.userList.map((user) => (
                                                            <option key={user.userId} value={user.userId}>{user.firstName} {user.lastName}</option>
                                                        ))}
                                                    </select>
                                                </td>
                                                <td nowrap="nowrap" align="right">
                                                    <label>Worked Date From</label>
                                                </td>
                                                <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                    <input type="date" name="workedDateFrom" value = {this.state.timesheetSO.workedDateFrom} onChange={this.handleChange} />
                                                </td>
                                                <td nowrap="nowrap" align="right">
                                                    <label>To</label>
                                                </td>
                                                <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                    <input type="date" name="workedDateTo" value = {this.state.timesheetSO.workedDateTo} onChange={this.handleChange} />
                                                </td>
                                                <td>
                                                    <input type="submit" value="Go" className="button" />
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table> 
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
                <div className="button-region">
                    <Link to='/TimesheetCreate' className="button">Create</Link>
                </div>
                <div className="report-region-2">
                    <table cellPadding="0" border="0" cellSpacing="0" summary="" className="report-standard">
                        <thead>
                            <tr >
                                <th></th>
                                <th></th>
                                <th>User</th>
                                <th>Job No</th>
                                <th>Job Name</th>
                                <th>Worked Date</th>
                                <th>Worked Day</th>
                                <th>Worked Hrs</th>
                                <th>Timesheet Code</th>
                                <th>Active</th>
                                <th>Date Created</th>
                                <th>User Created</th>
                                <th>Date Modified</th>
                                <th>User Modified</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.state.timesheetList.map((timesheet) => (
                                <tr key={timesheet.timesheetId} className="highlight-row">
                                    <td><Link to={`/TimesheetEdit/${timesheet.timesheetId}`}>Edit</Link></td>
                                    <td><a href="#" onClick = {this.deleteTimesheet.bind(this, timesheet.timesheetId)}>Delete</a></td>
                                    <td>{timesheet.userObj != null && timesheet.userObj.firstName} {timesheet.userObj != null && timesheet.userObj.lastName}</td>
                                    <td>{timesheet.jobNo}</td>
                                    <td>{timesheet.jobObj != null && timesheet.jobObj.jobName}</td>
                                    <td>{this.formatDate(timesheet.workedDate)}</td>
                                    <td>{this.formatDay(timesheet.workedDate)}</td>
                                    <td>{timesheet.workedHrs}</td>
                                    <td>{timesheet.timesheetCode}</td>
                                    {timesheet.active && <td align="center"><input type="checkbox" checked disabled /></td>}
                                    {!timesheet.active && <td align="center"><input type="checkbox" disabled /></td>}
                                    <td>{this.formatDateTime(timesheet.dateCrt)}</td>
                                    <td>{timesheet.userCrtObj != null && timesheet.userCrtObj.firstName} {timesheet.userCrtObj != null && timesheet.userCrtObj.lastName}</td>
                                    <td>{this.formatDateTime(timesheet.dateMod)}</td>
                                    <td>{timesheet.userModObj != null && timesheet.userModObj.firstName} {timesheet.userModObj != null && timesheet.userModObj.lastName}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default Timesheet;