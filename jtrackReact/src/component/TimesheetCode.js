import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import GenService from '../service/GenService';
import TimesheetCodeService from '../service/TimesheetCodeService';
import Message from './Message';

class TimesheetCode extends Component {

    constructor(props) {
        super(props)
        this.state = {
            timesheetCodeList: [],
            msgObj: {
              msg: '',
              isError: true
            }
        }
    }

    componentDidMount() {
        this.getTimesheetCodeList();
    }

    getTimesheetCodeList(){
        TimesheetCodeService.getTimesheetCodeList()
        .then(res => {
            this.updateMessage('');
            this.setState({ timesheetCodeList: res.data });
        })
        .catch(err => {
            this.updateMessage(err, true);
        });;
    }

    deleteTimesheetCode(timesheetCode){
        TimesheetCodeService.deleteTimesheetCode(timesheetCode)
        .then(res => {
            this.getTimesheetCodeList();
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

    render(){
        return (
            <div>
                <Message msgObj={this.state.msgObj} />
                <div className="button-region">
                    <Link to='/TimesheetCodeCreate' className="button">Create</Link>
                </div>
                <div className="report-region-2">
                    <table cellPadding="0" border="0" cellSpacing="0" summary="" className="report-standard">
                        <thead>
                            <tr >
                                <th></th>
                                <th></th>
                                <th>Timesheet Code</th>
                                <th>Timesheet Code Description</th>
                                <th>Active</th>
                                <th>Date Created</th>
                                <th>User Created</th>
                                <th>Date Modified</th>
                                <th>User Modified</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.state.timesheetCodeList.map((timesheetCode) => (
                                <tr key={timesheetCode.timesheetCode} className="highlight-row">
                                    <td><Link to={`/TimesheetCodeEdit/${timesheetCode.timesheetCode}`}>Edit</Link></td>
                                    <td><a href="#" onClick = {this.deleteTimesheetCode.bind(this, timesheetCode.timesheetCode)}>Delete</a></td>
                                    <td>{timesheetCode.timesheetCode}</td>
                                    <td>{timesheetCode.timesheetCodeDesc}</td>
                                    {timesheetCode.active && <td align="center"><input type="checkbox" checked disabled /></td>}
                                    {!timesheetCode.active && <td align="center"><input type="checkbox" disabled /></td>}
                                    <td>{this.formatDateTime(timesheetCode.dateCrt)}</td>
                                    <td>{timesheetCode.userCrtObj != null && timesheetCode.userCrtObj.firstName} {timesheetCode.userCrtObj != null && timesheetCode.userCrtObj.lastName}</td>
                                    <td>{this.formatDateTime(timesheetCode.dateMod)}</td>
                                    <td>{timesheetCode.userModObj != null && timesheetCode.userModObj.firstName} {timesheetCode.userModObj != null && timesheetCode.userModObj.lastName}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default TimesheetCode;