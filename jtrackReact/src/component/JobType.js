import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import GenService from '../service/GenService';
import JobTypeService from '../service/JobTypeService';
import Message from './Message';

class JobType extends Component {

    constructor(props) {
        super(props)
        this.state = {
            jobTypeList: [],
            msgObj: {
              msg: '',
              isError: true
            }
        }
    }

    componentDidMount() {
        this.getJobTypeList();
    }

    getJobTypeList(){
        JobTypeService.getJobTypeList()
        .then(res => {
            this.updateMessage('');
            this.setState({ jobTypeList: res.data });
        })
        .catch(err => {
            this.updateMessage(err, true);
        });;
    }

    deleteJobType(jobType){
        JobTypeService.deleteJobType(jobType)
        .then(res => {
            this.getJobTypeList();
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
                    <Link to='/JobTypeCreate' className="button">Create</Link>
                </div>
                <div className="report-region-2">
                    <table cellPadding="0" border="0" cellSpacing="0" summary="" className="report-standard">
                        <thead>
                            <tr >
                                <th></th>
                                <th></th>
                                <th>Job Type</th>
                                <th>Job Type Description</th>
                                <th>Active</th>
                                <th>Date Created</th>
                                <th>User Created</th>
                                <th>Date Modified</th>
                                <th>User Modified</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.state.jobTypeList.map((jobType) => (
                                <tr key={jobType.jobType} className="highlight-row">
                                    <td><Link to={`/JobTypeEdit/${jobType.jobType}`}>Edit</Link></td>
                                    <td><a href="#" onClick = {this.deleteJobType.bind(this, jobType.jobType)}>Delete</a></td>
                                    <td>{jobType.jobType}</td>
                                    <td>{jobType.jobTypeDesc}</td>
                                    {jobType.active && <td align="center"><input type="checkbox" checked disabled /></td>}
                                    {!jobType.active && <td align="center"><input type="checkbox" disabled /></td>}
                                    <td>{this.formatDateTime(jobType.dateCrt)}</td>
                                    <td>{jobType.userCrtObj != null && jobType.userCrtObj.firstName} {jobType.userCrtObj != null && jobType.userCrtObj.lastName}</td>
                                    <td>{this.formatDateTime(jobType.dateMod)}</td>
                                    <td>{jobType.userModObj != null && jobType.userModObj.firstName} {jobType.userModObj != null && jobType.userModObj.lastName}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default JobType;