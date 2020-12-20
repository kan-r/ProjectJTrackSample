import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Link } from 'react-router-dom';
import TimesheetService from '../service/TimesheetService';
import UserService from '../service/UserService';
import JobService from '../service/JobService';
import TimesheetCodeService from '../service/TimesheetCodeService';
import Message from './Message';

class TimesheetCreate extends Component {

    constructor(props) {
        super(props)
        this.state = {
            timesheet: {
                timesheetId: '',
                userId: '',
                jobNo: '',
                workedDate: '',
                workedHrs: '',
                timesheetCode: '',
                active: true,
                userCrt: ''
            },
            userList: [],
            jobList: [],
            timesheetCodeList: [],
            msgObj: {
              msg: '',
              isError: true
            }
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleChangeCheckBox = this.handleChangeCheckBox.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        this.getUserList();
        this.getJobList();
        this.getTimesheetCodeList();
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

    getJobList(){
        JobService.getJobList()
        .then(res => {
            this.updateMessage('');
            this.setState({ jobList: res.data })
        })
        .catch(err => {
            this.updateMessage(err, true);
        });
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

    handleChange(event) {
        
        let timesheet = {
            ...this.state.timesheet,
            [event.target.name] : event.target.value
        };

        this.setState({timesheet:timesheet});
    }

    handleChangeCheckBox(event) {
        
        let timesheet = {
            ...this.state.timesheet,
            active : !this.state.timesheet.active
        };

        this.setState({timesheet:timesheet});
    }

    handleSubmit(event) {
        event.preventDefault();

        let userId  = this.state.timesheet.userId;
        if(userId == null || userId.trim() === ''){
            this.updateMessage('User is required', true);
            return;
        }

        let jobNo  = this.state.timesheet.jobNo;
        if(jobNo == null || jobNo === ''){
            this.updateMessage('Job is required', true);
            return;
        }

        let workedDate  = this.state.timesheet.workedDate;
        if(workedDate == null || workedDate.trim() === ''){
            this.updateMessage('Worked Date is required', true);
            return;
        }

        TimesheetService.addTimesheet(this.state.timesheet)
        .then(res => {
            this.updateMessage('');
            try{
                this.props.history.push(`/Timesheet`);
            }catch(e){
                console.log(e);
            }
        })
        .catch(err => {
            this.updateMessage(err, true);
        });
    }

    updateMessage(msg, isError = false){
        this.setState({msgObj: {msg:msg, isError:isError}});
    }

    render(){
        return (
            <div className="App">
                <Message msgObj={this.state.msgObj} />
                <form onSubmit={this.handleSubmit}>
                    <div className="button-region">
                        <Link to='/Timesheet' className="button">Cancel</Link>
                        <input type="submit" value="Save" className="button" />
                    </div>
                    <div className="form-region-2">
                        <table cellPadding="0" border="0" cellSpacing="0" summary="" className="form-standard">
                            <thead>
                                <tr>
                                    <th><div>Create Timesheet</div></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <table border="0" summary="" >
                                            <tbody>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>User </label>
                                                    </td>
                                                    <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <select name="userId" value = {this.state.timesheet.userId} onChange={this.handleChange}>
                                                            <option value=""></option>
                                                            {this.state.userList.map((user) => (
                                                                <option key={user.userId} value={user.userId}>{user.firstName} {user.lastName}</option>
                                                            ))}
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job </label>
                                                    </td>
                                                    <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <select name="jobNo" value = {this.state.timesheet.jobNo} onChange={this.handleChange}>
                                                            <option value=""></option>
                                                            {this.state.jobList.map((job) => (
                                                                <option key={job.jobNo} value={job.jobNo}>{job.jobName} - {job.jobNo}</option>
                                                            ))}
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Worked Date</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input type="date" name="workedDate" value = {this.state.timesheet.workedDate} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Worked Hrs</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="workedHrs" value = {this.state.timesheet.workedHrs} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Timesheet Code </label>
                                                    </td>
                                                    <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <select name="timesheetCode" value = {this.state.timesheet.timesheetCode} onChange={this.handleChange}>
                                                            <option value=""></option>
                                                            {this.state.timesheetCodeList.map((timesheetCode) => (
                                                                <option key={timesheetCode.timesheetCode} value={timesheetCode.timesheetCode}>{timesheetCode.timesheetCode} - {timesheetCode.timesheetCodeDesc}</option>
                                                            ))}
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Active</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input type="checkbox" name="active" checked={this.state.timesheet.active} onChange={this.handleChangeCheckBox} />
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table> 
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
            </div>
        );
    }
}

export default withRouter(TimesheetCreate);