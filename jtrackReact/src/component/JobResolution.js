import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import GenService from '../service/GenService';
import JobResolutionService from '../service/JobResolutionService';
import Message from './Message';

class JobResolution extends Component {

    constructor(props) {
        super(props)
        this.state = {
            jobResolutionList: [],
            msgObj: {
              msg: '',
              isError: true
            }
        }
    }

    componentDidMount() {
        this.getJobResolutionList();
    }

    getJobResolutionList(){
        JobResolutionService.getJobResolutionList()
        .then(res => {
            this.updateMessage('');
            this.setState({ jobResolutionList: res.data });
        })
        .catch(err => {
            this.updateMessage(err, true);
        });;
    }

    deleteJobResolution(jobResolution){
        JobResolutionService.deleteJobResolution(jobResolution)
        .then(res => {
            this.getJobResolutionList();
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
                    <Link to='/JobResolutionCreate' className="button">Create</Link>
                </div>
                <div className="report-region-2">
                    <table cellPadding="0" border="0" cellSpacing="0" summary="" className="report-standard">
                        <thead>
                            <tr >
                                <th></th>
                                <th></th>
                                <th>Job Resolution</th>
                                <th>Job Resolution Description</th>
                                <th>Active</th>
                                <th>Date Created</th>
                                <th>User Created</th>
                                <th>Date Modified</th>
                                <th>User Modified</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.state.jobResolutionList.map((jobResolution) => (
                                <tr key={jobResolution.jobResolution} className="highlight-row">
                                    <td><Link to={`/JobResolutionEdit/${jobResolution.jobResolution}`}>Edit</Link></td>
                                    <td><a href="#" onClick = {this.deleteJobResolution.bind(this, jobResolution.jobResolution)}>Delete</a></td>
                                    <td>{jobResolution.jobResolution}</td>
                                    <td>{jobResolution.jobResolutionDesc}</td>
                                    {jobResolution.active && <td align="center"><input type="checkbox" checked disabled /></td>}
                                    {!jobResolution.active && <td align="center"><input type="checkbox" disabled /></td>}
                                    <td>{this.formatDateTime(jobResolution.dateCrt)}</td>
                                    <td>{jobResolution.userCrtObj != null && jobResolution.userCrtObj.firstName} {jobResolution.userCrtObj != null && jobResolution.userCrtObj.lastName}</td>
                                    <td>{this.formatDateTime(jobResolution.dateMod)}</td>
                                    <td>{jobResolution.userModObj != null && jobResolution.userModObj.firstName} {jobResolution.userModObj != null && jobResolution.userModObj.lastName}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default JobResolution;