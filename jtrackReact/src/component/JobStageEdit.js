import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Link } from 'react-router-dom';
import JobStageService from '../service/JobStageService';
import Message from './Message';

class JobStageCreate extends Component {

    constructor(props) {
        super(props)
        this.state = {
            jobStage: {
                jobStage: '',
                jobStageDesc: '',
                active: true,
                userMod: ''
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

    componentDidMount() {
        const { match: { params } } = this.props;
        let jobStage = params.jobStage;
        this.getJobStage(jobStage);
    }

    getJobStage(jobStage){
        JobStageService.getJobStage(jobStage)
        .then(res => {
            this.updateMessage('');
            this.setState({ jobStage: res.data })
        })
        .catch(err => {
            this.updateMessage(err, true);
        });;
    }

    handleChange(event) {
        
        let jobStage = {
            ...this.state.jobStage,
            [event.target.name] : event.target.value
        };

        this.setState({jobStage:jobStage});
    }

    handleChangeCheckBox(event) {
        
        let jobStage = {
            ...this.state.jobStage,
            active : !this.state.jobStage.active
        };

        this.setState({jobStage:jobStage});
    }

    handleSubmit(event) {
        event.preventDefault();

        JobStageService.updateJobStage(this.state.jobStage)
        .then(res => {
            this.updateMessage('');
            try{
                this.props.history.push(`/JobStage`);
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
                        <Link to='/JobStage' className="button">Cancel</Link>
                        <input type="submit" value="Save" className="button" />
                    </div>
                    <div className="form-region-2">
                        <table cellPadding="0" border="0" cellSpacing="0" summary="" className="form-standard">
                            <thead>
                                <tr>
                                    <th><div>Edit Job Stage</div></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <table border="0" summary="" >
                                            <tbody>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Stage</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="jobStage" value={this.state.jobStage.jobStage} onChange={this.handleChange} disabled={true} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Stage Description</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <textarea name="jobStageDesc" value={this.state.jobStage.jobStageDesc} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Active</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input type="checkbox" name="active" checked={this.state.jobStage.active} onChange={this.handleChangeCheckBox} />
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

export default withRouter(JobStageCreate);