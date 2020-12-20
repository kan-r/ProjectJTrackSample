import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import GenService from '../service/GenService';
import JobStageService from '../service/JobStageService';
import Message from './Message';

class JobStage extends Component {

    constructor(props) {
        super(props)
        this.state = {
            jobStageList: [],
            msgObj: {
              msg: '',
              isError: true
            }
        }
    }

    componentDidMount() {
        this.getJobStageList();
    }

    getJobStageList(){
        JobStageService.getJobStageList()
        .then(res => {
            this.updateMessage('');
            this.setState({ jobStageList: res.data });
        })
        .catch(err => {
            this.updateMessage(err, true);
        });;
    }

    deleteJobStage(jobStage){
        JobStageService.deleteJobStage(jobStage)
        .then(res => {
            this.getJobStageList();
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
                    <Link to='/JobStageCreate' className="button">Create</Link>
                </div>
                <div className="report-region-2">
                    <table cellPadding="0" border="0" cellSpacing="0" summary="" className="report-standard">
                        <thead>
                            <tr >
                                <th></th>
                                <th></th>
                                <th>Job Stage</th>
                                <th>Job Stage Description</th>
                                <th>Active</th>
                                <th>Date Created</th>
                                <th>User Created</th>
                                <th>Date Modified</th>
                                <th>User Modified</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.state.jobStageList.map((jobStage) => (
                                <tr key={jobStage.jobStage} className="highlight-row">
                                    <td><Link to={`/JobStageEdit/${jobStage.jobStage}`}>Edit</Link></td>
                                    <td><a href="#" onClick = {this.deleteJobStage.bind(this, jobStage.jobStage)}>Delete</a></td>
                                    <td>{jobStage.jobStage}</td>
                                    <td>{jobStage.jobStageDesc}</td>
                                    {jobStage.active && <td align="center"><input type="checkbox" checked disabled /></td>}
                                    {!jobStage.active && <td align="center"><input type="checkbox" disabled /></td>}
                                    <td>{this.formatDateTime(jobStage.dateCrt)}</td>
                                    <td>{jobStage.userCrtObj != null && jobStage.userCrtObj.firstName} {jobStage.userCrtObj != null && jobStage.userCrtObj.lastName}</td>
                                    <td>{this.formatDateTime(jobStage.dateMod)}</td>
                                    <td>{jobStage.userModObj != null && jobStage.userModObj.firstName} {jobStage.userModObj != null && jobStage.userModObj.lastName}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default JobStage;