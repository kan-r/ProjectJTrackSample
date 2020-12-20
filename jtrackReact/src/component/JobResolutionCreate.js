import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Link } from 'react-router-dom';
import JobResolutionService from '../service/JobResolutionService';
import Message from './Message';

class JobResolutionCreate extends Component {

    constructor(props) {
        super(props)
        this.state = {
            jobResolution: {
                jobResolution: '',
                jobResolutionDesc: '',
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
        
        let jobResolution = {
            ...this.state.jobResolution,
            [event.target.name] : event.target.value
        };

        this.setState({jobResolution:jobResolution});
    }

    handleChangeCheckBox(event) {
        
        let jobResolution = {
            ...this.state.jobResolution,
            active : !this.state.jobResolution.active
        };

        this.setState({jobResolution:jobResolution});
    }

    handleSubmit(event) {
        event.preventDefault();

        let jobResolution  = this.state.jobResolution.jobResolution;
        if(jobResolution == null || jobResolution.trim() === ''){
            this.updateMessage('Job Resolution is required', true);
            return;
        }

        JobResolutionService.addJobResolution(this.state.jobResolution)
        .then(res => {
            this.updateMessage('');
            try{
                this.props.history.push(`/JobResolution`);
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
                        <Link to='/JobResolution' className="button">Cancel</Link>
                        <input type="submit" value="Save" className="button" />
                    </div>
                    <div className="form-region-2">
                        <table cellPadding="0" border="0" cellSpacing="0" summary="" className="form-standard">
                            <thead>
                                <tr>
                                    <th><div>Create Job Resolution</div></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <table border="0" summary="" >
                                            <tbody>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Resolution</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="jobResolution" value={this.state.jobResolution.jobResolution} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Resolution Description</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="jobResolutionDesc" value={this.state.jobResolution.jobResolutionDesc} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Active</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input type="checkbox" name="active" checked={this.state.jobResolution.active} onChange={this.handleChangeCheckBox} />
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

export default withRouter(JobResolutionCreate);