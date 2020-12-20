import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import GenService from '../service/GenService';
import JobStatusService from '../service/JobStatusService';
import Message from './Message';

class JobStatus extends Component {

    constructor(props) {
        super(props)
        this.state = {
            jobStatusList: [],
            msgObj: {
              msg: '',
              isError: true
            }
        }
    }

    componentDidMount() {
        this.getJobStatusList();
    }

    getJobStatusList(){
        JobStatusService.getJobStatusList()
        .then(res => {
            this.updateMessage('');
            this.setState({ jobStatusList: res.data });
        })
        .catch(err => {
            this.updateMessage(err, true);
        });;
    }

    deleteJobStatus(jobStatus){
        JobStatusService.deleteJobStatus(jobStatus)
        .then(res => {
            this.getJobStatusList();
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
                    <Link to='/JobStatusCreate' className="button">Create</Link>
                </div>
                <div className="report-region-2">
                    <table cellPadding="0" border="0" cellSpacing="0" summary="" className="report-standard">
                        <thead>
                            <tr >
                                <th></th>
                                <th></th>
                                <th>Job Status</th>
                                <th>Job Status Description</th>
                                <th>Active</th>
                                <th>Date Created</th>
                                <th>User Created</th>
                                <th>Date Modified</th>
                                <th>User Modified</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.state.jobStatusList.map((jobStatus) => (
                                <tr key={jobStatus.jobStatus} className="highlight-row">
                                    <td><Link to={`/JobStatusEdit/${jobStatus.jobStatus}`}>Edit</Link></td>
                                    <td><a href="#" onClick = {this.deleteJobStatus.bind(this, jobStatus.jobStatus)}>Delete</a></td>
                                    <td>{jobStatus.jobStatus}</td>
                                    <td>{jobStatus.jobStatusDesc}</td>
                                    {jobStatus.active && <td align="center"><input type="checkbox" checked disabled /></td>}
                                    {!jobStatus.active && <td align="center"><input type="checkbox" disabled /></td>}
                                    <td>{this.formatDateTime(jobStatus.dateCrt)}</td>
                                    <td>{jobStatus.userCrtObj != null && jobStatus.userCrtObj.firstName} {jobStatus.userCrtObj != null && jobStatus.userCrtObj.lastName}</td>
                                    <td>{this.formatDateTime(jobStatus.dateMod)}</td>
                                    <td>{jobStatus.userModObj != null && jobStatus.userModObj.firstName} {jobStatus.userModObj != null && jobStatus.userModObj.lastName}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default JobStatus;