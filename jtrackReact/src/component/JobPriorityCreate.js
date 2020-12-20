import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Link } from 'react-router-dom';
import JobPriorityService from '../service/JobPriorityService';
import Message from './Message';

class JobPriorityCreate extends Component {

    constructor(props) {
        super(props)
        this.state = {
            jobPriority: {
                jobPriority: '',
                jobPriorityDesc: '',
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
        
        let jobPriority = {
            ...this.state.jobPriority,
            [event.target.name] : event.target.value
        };

        this.setState({jobPriority:jobPriority});
    }

    handleChangeCheckBox(event) {
        
        let jobPriority = {
            ...this.state.jobPriority,
            active : !this.state.jobPriority.active
        };

        this.setState({jobPriority:jobPriority});
    }

    handleSubmit(event) {
        event.preventDefault();

        let jobPriority  = this.state.jobPriority.jobPriority;
        if(jobPriority == null || jobPriority.trim() === ''){
            this.updateMessage('Job Priority is required', true);
            return;
        }

        JobPriorityService.addJobPriority(this.state.jobPriority)
        .then(res => {
            this.updateMessage('');
            try{
                this.props.history.push(`/JobPriority`);
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
                        <Link to='/JobPriority' className="button">Cancel</Link>
                        <input type="submit" value="Save" className="button" />
                    </div>
                    <div className="form-region-2">
                        <table cellPadding="0" border="0" cellSpacing="0" summary="" className="form-standard">
                            <thead>
                                <tr>
                                    <th><div>Create Job Priority</div></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <table border="0" summary="" >
                                            <tbody>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Priority</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="jobPriority" value={this.state.jobPriority.jobPriority} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Priority Description</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="jobPriorityDesc" value={this.state.jobPriority.jobPriorityDesc} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Active</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input type="checkbox" name="active" checked={this.state.jobPriority.active} onChange={this.handleChangeCheckBox} />
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

export default withRouter(JobPriorityCreate);