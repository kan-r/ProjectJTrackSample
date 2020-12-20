import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import GenService from '../service/GenService';
import JobPriorityService from '../service/JobPriorityService';
import Message from './Message';

class JobPriority extends Component {

    constructor(props) {
        super(props)
        this.state = {
            jobPriorityList: [],
            msgObj: {
              msg: '',
              isError: true
            }
        }
    }

    componentDidMount() {
        this.getJobPriorityList();
    }

    getJobPriorityList(){
        JobPriorityService.getJobPriorityList()
        .then(res => {
            this.updateMessage('');
            this.setState({ jobPriorityList: res.data });
        })
        .catch(err => {
            this.updateMessage(err, true);
        });;
    }

    deleteJobPriority(jobPriority){
        JobPriorityService.deleteJobPriority(jobPriority)
        .then(res => {
            this.getJobPriorityList();
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
                    <Link to='/JobPriorityCreate' className="button">Create</Link>
                </div>
                <div className="report-region-2">
                    <table cellPadding="0" border="0" cellSpacing="0" summary="" className="report-standard">
                        <thead>
                            <tr >
                                <th></th>
                                <th></th>
                                <th>Job Priority</th>
                                <th>Job Priority Description</th>
                                <th>Active</th>
                                <th>Date Created</th>
                                <th>User Created</th>
                                <th>Date Modified</th>
                                <th>User Modified</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.state.jobPriorityList.map((jobPriority) => (
                                <tr key={jobPriority.jobPriority} className="highlight-row">
                                    <td><Link to={`/JobPriorityEdit/${jobPriority.jobPriority}`}>Edit</Link></td>
                                    <td><a href="#" onClick = {this.deleteJobPriority.bind(this, jobPriority.jobPriority)}>Delete</a></td>
                                    <td>{jobPriority.jobPriority}</td>
                                    <td>{jobPriority.jobPriorityDesc}</td>
                                    {jobPriority.active && <td align="center"><input type="checkbox" checked disabled /></td>}
                                    {!jobPriority.active && <td align="center"><input type="checkbox" disabled /></td>}
                                    <td>{this.formatDateTime(jobPriority.dateCrt)}</td>
                                    <td>{jobPriority.userCrtObj != null && jobPriority.userCrtObj.firstName} {jobPriority.userCrtObj != null && jobPriority.userCrtObj.lastName}</td>
                                    <td>{this.formatDateTime(jobPriority.dateMod)}</td>
                                    <td>{jobPriority.userModObj != null && jobPriority.userModObj.firstName} {jobPriority.userModObj != null && jobPriority.userModObj.lastName}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default JobPriority;