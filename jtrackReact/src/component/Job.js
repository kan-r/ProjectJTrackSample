import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import GenService from '../service/GenService';
import JobService from '../service/JobService';
import JobTypeService from '../service/JobTypeService';
import JobStatusService from '../service/JobStatusService';
import UserService from '../service/UserService';
import Message from './Message';

class Job extends Component {

    constructor(props) {
        super(props)
        this.state = {
            jobList: [],
            jobTypeList: [],
            jobStatusList: [],
            assignedToList: [],
            jobSO: {
                jobName: '',
                jobType: '',
                jobStatus: '',
                assignedTo: '',
                includeChildJobs: true,
                jobNameChild: '',
                jobTypeChild: '',
                jobStatusChild: '',
                assignedToChild: '',
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
        // this.getJobList();
        this.getJobList2(this.state.jobSO);
        this.getJobTypeList();
        this.getJobStatusList();
        this.getAssignedToList();
    }

    handleSubmit(event) {
        event.preventDefault();
        this.getJobList2(this.state.jobSO);
    }

    handleChange(event) {

        let jobSO = {
            ...this.state.jobSO,
            [event.target.name] : event.target.value
        };

        this.setState({jobSO:jobSO});
    }

    handleChangeCheckBox(event) {
        
        let jobSO = {
            ...this.state.jobSO,
            includeChildJobs : !this.state.jobSO.includeChildJobs
        };

        this.setState({jobSO:jobSO});
    }

    getJobList(){
        JobService.getJobList()
        .then(res => {
            this.updateMessage('');
            this.setState({ jobList: res.data });
        })
        .catch(err => {
            this.updateMessage(err, true);
        });;
    }

    getJobList2(jobSO){
        JobService.getJobList2(jobSO)
        .then(res => {
            this.updateMessage('');
            this.setState({ jobList: res.data });
        })
        .catch(err => {
            this.updateMessage(err, true);
        });;
    }

    deleteJob(jobNo){
        JobService.deleteJob(jobNo)
        .then(res => {
            this.getJobList2(this.state.jobSO);
        })
        .catch(err => {
            this.updateMessage(err, true);
        });
    }

    getAssignedToList(){
        UserService.getUserList()
        .then(res => {
            this.updateMessage('');
            this.setState({ assignedToList: res.data })
        })
        .catch(err => {
            this.updateMessage(err, true);
        });
    }

    getJobTypeList(){
        JobTypeService.getJobTypeList()
        .then(res => {
            this.updateMessage('');
            this.setState({ jobTypeList: res.data })
        })
        .catch(err => {
            this.updateMessage(err, true);
        });
    }

    getJobStatusList(){
        JobStatusService.getJobStatusList()
        .then(res => {
            this.updateMessage('');
            this.setState({ jobStatusList: res.data })
        })
        .catch(err => {
            this.updateMessage(err, true);
        });
    }

    updateMessage(msg, isError = false){
        this.setState({msgObj: {msg:msg, isError:isError}});
    }

    formatDate(dtTime){
        return GenService.formatDate(dtTime);
    }

    render(){
        return (
            <div>
                <Message msgObj={this.state.msgObj} />
                <div className="form-region">
                    <form onSubmit={this.handleSubmit}>
                        <table cellPadding="0" border="1" cellSpacing="0" summary="" className="form-standard">
                            <tbody>
                            <tr>
                                <td>
                                    <table border="0" summary="" >
                                        <tbody>
                                            <tr>
                                                <td nowrap="nowrap" align="right">
                                                    <label>Job Name</label>
                                                </td>
                                                <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                    <input name="jobName" value = {this.state.jobSO.jobName} onChange={this.handleChange} />
                                                </td>
                                                <td nowrap="nowrap" align="right">
                                                    <label>Job Type</label>
                                                </td>
                                                <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                    <select name="jobType" value = {this.state.jobSO.jobType} onChange={this.handleChange}>
                                                        <option value=""></option>
                                                        {this.state.jobTypeList.map((jobType) => (
                                                            <option key={jobType.jobType} value={jobType.jobType}>{jobType.jobType}</option>
                                                        ))}
                                                    </select>
                                                </td>
                                                <td nowrap="nowrap" align="right">
                                                    <label>Include Child Jobs</label>
                                                </td>
                                                <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                    <input type="checkbox" name="includeChildJobs" checked={this.state.jobSO.includeChildJobs} onChange={this.handleChangeCheckBox} />
                                                </td>
                                                <td nowrap="nowrap" align="right">
                                                    <label>==></label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td nowrap="nowrap" align="right">
                                                    <label>Job Status</label>
                                                </td>
                                                <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                    <select name="jobStatus" value = {this.state.jobSO.jobStatus} onChange={this.handleChange}>
                                                        <option value=""></option>
                                                        {this.state.jobStatusList.map((jobStatus) => (
                                                            <option key={jobStatus.jobStatus} value={jobStatus.jobStatus}>{jobStatus.jobStatus}</option>
                                                        ))}
                                                    </select>
                                                </td>
                                                <td nowrap="nowrap" align="right">
                                                    <label>assignedTo</label>
                                                </td>
                                                <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                    <select name="assignedTo" value = {this.state.jobSO.assignedTo} onChange={this.handleChange}>
                                                        <option value=""></option>
                                                        {this.state.assignedToList.map((assignedTo) => (
                                                            <option key={assignedTo.userId} value={assignedTo.userId}>{assignedTo.firstName} {assignedTo.lastName}</option>
                                                        ))}
                                                    </select>
                                                </td>
                                                <td>
                                                    <input type="submit" value="Go" className="button" />
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table> 
                                </td>
                                <td>
                                    <table border="0" summary="">
                                        <tbody>
                                            <tr>
                                                <td nowrap="nowrap" align="right">
                                                    <label>Job Name</label>
                                                </td>
                                                <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                    <input name="jobNameChild" value = {this.state.jobSO.jobNameChild} disabled={!this.state.jobSO.includeChildJobs} onChange={this.handleChange} />
                                                </td>
                                                <td nowrap="nowrap" align="right">
                                                    <label>Job Type</label>
                                                </td>
                                                <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                    <select name="jobTypeChild" value = {this.state.jobSO.jobTypeChild} disabled={!this.state.jobSO.includeChildJobs} onChange={this.handleChange}>
                                                        <option value=""></option>
                                                        {this.state.jobTypeList.map((jobType) => (
                                                            <option key={jobType.jobType} value={jobType.jobType}>{jobType.jobType}</option>
                                                        ))}
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td nowrap="nowrap" align="right">
                                                    <label>Job Status</label>
                                                </td>
                                                <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                    <select name="jobStatusChild" value = {this.state.jobSO.jobStatusChild} disabled={!this.state.jobSO.includeChildJobs} onChange={this.handleChange}>
                                                        <option value=""></option>
                                                        {this.state.jobStatusList.map((jobStatus) => (
                                                            <option key={jobStatus.jobStatus} value={jobStatus.jobStatus}>{jobStatus.jobStatus}</option>
                                                        ))}
                                                    </select>
                                                </td>
                                                <td nowrap="nowrap" align="right">
                                                    <label>assignedTo</label>
                                                </td>
                                                <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                    <select name="assignedToChild" value = {this.state.jobSO.assignedToChild} disabled={!this.state.jobSO.includeChildJobs} onChange={this.handleChange}>
                                                        <option value=""></option>
                                                        {this.state.assignedToList.map((assignedTo) => (
                                                            <option key={assignedTo.userId} value={assignedTo.userId}>{assignedTo.firstName} {assignedTo.lastName}</option>
                                                        ))}
                                                    </select>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table> 
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
                <div className="button-region">
                    <Link to='/JobCreate' className="button">Create</Link>
                </div>
                <div className="report-region-2">
                    <table cellPadding="0" border="0" cellSpacing="0" summary="" className="report-standard">
                        <thead>
                            <tr >
                                <th></th>
                                <th></th>
                                <th>Job No</th>
                                <th>Parent Job Name</th>
                                <th>Job Name</th>
                                <th>Job Type</th>
                                <th>Job Stage</th>
                                <th>Job Priority</th>
                                <th>Job Status</th>
                                <th>Job Resolution</th>
                                <th>Job Order</th>
                                <th>Assigned To</th>
                                <th>Timesheet Code</th>
                                <th>Estimated Start Date</th>
                                <th>Actual Start Date</th>
                                <th>Estimated End Date</th>
                                <th>Actual End Date</th>
                                <th>Estimated Hrs</th>
                                <th>Completed Hrs</th>
                                <th>Active</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.state.jobList.map((job) => (
                                <tr key={job.jobNo} className="highlight-row">
                                    <td><Link to={`/JobEdit/${job.jobNo}`}>Edit</Link></td>
                                    <td><a href="#" onClick = {this.deleteJob.bind(this, job.jobNo)}>Delete</a></td>
                                    <td>{job.jobNo}</td>
                                    <td>{job.parentJobObj != null && job.parentJobObj.jobName}</td>
                                    <td>{job.jobName}</td>
                                    <td>{job.jobType}</td>
                                    <td>{job.jobStage}</td>
                                    <td>{job.jobPriority}</td>
                                    <td>{job.jobStatus}</td>
                                    <td>{job.jobResolution}</td>
                                    <td>{job.jobOrder}</td>
                                    <td>{job.assignedToObj != null && job.assignedToObj.firstName} {job.assignedToObj != null && job.assignedToObj.lastName}</td>
                                    <td>{job.timesheetCode}</td>
                                    <td>{this.formatDate(job.estimatedStartDate)}</td>
                                    <td>{this.formatDate(job.actualStartDate)}</td>
                                    <td>{this.formatDate(job.estimatedEndDate)}</td>
                                    <td>{this.formatDate(job.estimatedEndDate)}</td>
                                    <td>{job.estimatedHrs}</td>
                                    <td>{job.completedHrs}</td>
                                    {job.active && <td align="center"><input type="checkbox" checked disabled /></td>}
                                    {!job.active && <td align="center"><input type="checkbox" disabled /></td>}
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default Job;