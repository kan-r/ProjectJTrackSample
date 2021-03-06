import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Link } from 'react-router-dom';
import TimesheetCodeService from '../service/TimesheetCodeService';
import Message from './Message';

class TimesheetCodeCreate extends Component {

    constructor(props) {
        super(props)
        this.state = {
            timesheetCode: {
                timesheetCode: '',
                timesheetCodeDesc: '',
                active: true,
                userCrt: ''
            },
            msgObj: {
              msg: '',
              isError: true
            }
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleChangeCheckBox = this.handleChangeCheckBox.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        let value = event.target.value;
        if(event.target.name === 'timesheetCode'){
            value = value.toUpperCase();
        }

        let timesheetCode = {
            ...this.state.timesheetCode,
            [event.target.name] : value
        };

        this.setState({timesheetCode:timesheetCode});
    }

    handleChangeCheckBox(event) {
        
        let timesheetCode = {
            ...this.state.timesheetCode,
            active : !this.state.timesheetCode.active
        };

        this.setState({timesheetCode:timesheetCode});
    }

    handleSubmit(event) {
        event.preventDefault();

        let timesheetCode  = this.state.timesheetCode.timesheetCode;
        if(timesheetCode == null || timesheetCode.trim() === ''){
            this.updateMessage('Timesheet Code is required', true);
            return;
        }

        TimesheetCodeService.addTimesheetCode(this.state.timesheetCode)
        .then(res => {
            this.updateMessage('');
            try{
                this.props.history.push(`/TimesheetCode`);
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
                        <Link to='/TimesheetCode' className="button">Cancel</Link>
                        <input type="submit" value="Save" className="button" />
                    </div>
                    <div className="form-region-2">
                        <table cellPadding="0" border="0" cellSpacing="0" summary="" className="form-standard">
                            <thead>
                                <tr>
                                    <th><div>Create Timesheet Code</div></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <table border="0" summary="" >
                                            <tbody>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Timesheet Code</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="timesheetCode" value={this.state.timesheetCode.timesheetCode} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Timesheet Code Description</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="timesheetCodeDesc" value={this.state.timesheetCode.timesheetCodeDesc} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Active</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input type="checkbox" name="active" checked={this.state.timesheetCode.active} onChange={this.handleChangeCheckBox} />
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

export default withRouter(TimesheetCodeCreate);