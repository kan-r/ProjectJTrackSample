import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Link } from 'react-router-dom';
import JobStatusService from '../service/JobStatusService';
import Message from './Message';

class JobStatusCreate extends Component {

    constructor(props) {
        super(props)
        this.state = {
            jobStatus: {
                jobStatus: '',
                jobStatusDesc: '',
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
        
        let jobStatus = {
            ...this.state.jobStatus,
            [event.target.name] : event.target.value
        };

        this.setState({jobStatus:jobStatus});
    }

    handleChangeCheckBox(event) {
        
        let jobStatus = {
            ...this.state.jobStatus,
            active : !this.state.jobStatus.active
        };

        this.setState({jobStatus:jobStatus});
    }

    handleSubmit(event) {
        event.preventDefault();

        let jobStatus  = this.state.jobStatus.jobStatus;
        if(jobStatus == null || jobStatus.trim() === ''){
            this.updateMessage('Job Status is required', true);
            return;
        }

        JobStatusService.addJobStatus(this.state.jobStatus)
        .then(res => {
            this.updateMessage('');
            try{
                this.props.history.push(`/JobStatus`);
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
                        <Link to='/JobStatus' className="button">Cancel</Link>
                        <input type="submit" value="Save" className="button" />
                    </div>
                    <div className="form-region-2">
                        <table cellPadding="0" border="0" cellSpacing="0" summary="" className="form-standard">
                            <thead>
                                <tr>
                                    <th><div>Create Job Status</div></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <table border="0" summary="" >
                                            <tbody>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Status</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="jobStatus" value={this.state.jobStatus.jobStatus} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Status Description</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <textarea name="jobStatusDesc" value={this.state.jobStatus.jobStatusDesc} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Active</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input type="checkbox" name="active" checked={this.state.jobStatus.active} onChange={this.handleChangeCheckBox} />
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

export default withRouter(JobStatusCreate);