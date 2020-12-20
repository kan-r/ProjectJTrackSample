import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Link } from 'react-router-dom';
import JobService from '../service/JobService';
import JobTypeService from '../service/JobTypeService';
import JobStageService from '../service/JobStageService';
import JobPriorityService from '../service/JobPriorityService';
import JobStatusService from '../service/JobStatusService';
import JobResolutionService from '../service/JobResolutionService';
import UserService from '../service/UserService';
import TimesheetCodeService from '../service/TimesheetCodeService';
import Message from './Message';

class JobEdit extends Component {

    constructor(props) {
        super(props)
        this.state = {
            job: {
                jobNo: '',
                jobName: '',
                jobDesc: '',
                jobType: '',
                jobPriority: '',
                jobStatus: '',
                jobResolution: '',
                jobStage: '',
                jobOrder: '',
                assignedTo: null,
                timesheetCode: '',
                estimatedHrs: '',
                completedHrs: '',
                estimatedStartDate: '',
                actualStartDate: '',
                estimatedEndDate: '',
                actualEndDate: '',
                parentJobNo: '',
                active: true,
                userMod: '',
                jobRef: ''
            },
            jobTypeList: [],
            jobStageList: [],
            jobPriorityList: [],
            jobStatusList: [],
            jobResolutionList: [],
            assignedToList: [],
            timesheetCodeList: [],
            parentJobList: [],
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
        let jobNo = params.jobNo;

        this.getJob(jobNo);
        this.getJobTypeList();
        this.getJobStageList();
        this.getJobPriorityList();
        this.getJobStatusList();
        this.getJobResolutionList();
        this.getAssignedToList();
        this.getTimesheetCodeList();
        this.getParentJobList();
    }

    getJob(jobNo){
        JobService.getJob(jobNo)
        .then(res => {
            this.updateMessage('');
            this.setState({ job: res.data });
            console.log(this.state.job);
        })
        .catch(err => {
            this.updateMessage(err, true);
        });;
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

    getJobStageList(){
        JobStageService.getJobStageList()
        .then(res => {
            this.updateMessage('');
            this.setState({ jobStageList: res.data })
        })
        .catch(err => {
            this.updateMessage(err, true);
        });
    }

    getJobPriorityList(){
        JobPriorityService.getJobPriorityList()
        .then(res => {
            this.updateMessage('');
            this.setState({ jobPriorityList: res.data })
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

    getJobResolutionList(){
        JobResolutionService.getJobResolutionList()
        .then(res => {
            this.updateMessage('');
            this.setState({ jobResolutionList: res.data })
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

    getTimesheetCodeList(){
        TimesheetCodeService.getTimesheetCodeList()
        .then(res => {
            this.updateMessage('');
            this.setState({ timesheetCodeList: res.data });
        })
        .catch(err => {
            this.updateMessage(err, true);
        });;
    }

    getParentJobList(){
        JobService.getParentJobList()
        .then(res => {
            this.updateMessage('');
            this.setState({ parentJobList: res.data })
        })
        .catch(err => {
            this.updateMessage(err, true);
        });
    }

    handleChange(event) {
        
        let job = {
            ...this.state.job,
            [event.target.name] : event.target.value
        };

        if(job.assignedTo === ''){
            job.assignedTo = null;
        }

        this.setState({job:job});
    }

    handleChangeCheckBox(event) {
        
        let job = {
            ...this.state.job,
            active : !this.state.job.active
        };

        this.setState({job:job});
    }

    handleSubmit(event) {
        event.preventDefault();

        let jobName  = this.state.job.jobName;
        if(jobName == null || jobName.trim() === ''){
            this.updateMessage('Job Name is required', true);
            return;
        }

        JobService.updateJob(this.state.job)
        .then(res => {
            this.updateMessage('');
            try{
                this.props.history.push(`/Job`);
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
                        <Link to='/Job' className="button">Cancel</Link>
                        <input type="submit" value="Save" className="button" />
                    </div>
                    <div className="form-region-2">
                        <table cellPadding="0" border="0" cellSpacing="0" summary="" className="form-standard">
                            <thead>
                                <tr>
                                    <th><div>Edit Job</div></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <table border="0" summary="" >
                                            <tbody>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job No</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="jobNo" value={this.state.job.jobNo} disabled={true} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Name</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="jobName" value = {this.state.job.jobName} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Description</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <textarea name="jobDesc" value = {this.state.job.jobDesc} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Reference</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="jobRef" value = {this.state.job.jobRef} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Type</label>
                                                    </td>
                                                    <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <select name="jobType" value = {this.state.job.jobType} onChange={this.handleChange}>
                                                            <option value=""></option>
                                                            {this.state.jobTypeList.map((jobType) => (
                                                                <option key={jobType.jobType} value={jobType.jobType}>{jobType.jobType}</option>
                                                            ))}
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Stage</label>
                                                    </td>
                                                    <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <select name="jobStage" value = {this.state.job.jobStage} onChange={this.handleChange}>
                                                            <option value=""></option>
                                                            {this.state.jobStageList.map((jobStage) => (
                                                                <option key={jobStage.jobStage} value={jobStage.jobStage}>{jobStage.jobStage}</option>
                                                            ))}
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Priority</label>
                                                    </td>
                                                    <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <select name="jobPriority" value = {this.state.job.jobPriority} onChange={this.handleChange}>
                                                            <option value=""></option>
                                                            {this.state.jobPriorityList.map((jobPriority) => (
                                                                <option key={jobPriority.jobPriority} value={jobPriority.jobPriority}>{jobPriority.jobPriority}</option>
                                                            ))}
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Status</label>
                                                    </td>
                                                    <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <select name="jobStatus" value = {this.state.job.jobStatus} onChange={this.handleChange}>
                                                            <option value=""></option>
                                                            {this.state.jobStatusList.map((jobStatus) => (
                                                                <option key={jobStatus.jobStatus} value={jobStatus.jobStatus}>{jobStatus.jobStatus}</option>
                                                            ))}
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Resolution</label>
                                                    </td>
                                                    <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <select name="jobResolution" value = {this.state.job.jobResolution} onChange={this.handleChange}>
                                                            <option value=""></option>
                                                            {this.state.jobResolutionList.map((jobResolution) => (
                                                                <option key={jobResolution.jobResolution} value={jobResolution.jobResolution}>{jobResolution.jobResolution}</option>
                                                            ))}
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Job Order</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="jobOrder" value = {this.state.job.jobOrder} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Assigned To</label>
                                                    </td>
                                                    <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <select name="assignedTo" value = {this.state.job.assignedTo} onChange={this.handleChange}>
                                                            <option value=""></option>
                                                            {this.state.assignedToList.map((assignedTo) => (
                                                                <option key={assignedTo.userId} value={assignedTo.userId}>{assignedTo.firstName} {assignedTo.lastName}</option>
                                                            ))}
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Estimated Hrs</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="estimatedHrs" value = {this.state.job.estimatedHrs} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Timesheet Code</label>
                                                    </td>
                                                    <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <select name="timesheetCode" value = {this.state.job.timesheetCode} onChange={this.handleChange}>
                                                            <option value=""></option>
                                                            {this.state.timesheetCodeList.map((timesheetCode) => (
                                                                <option key={timesheetCode.timesheetCode} value={timesheetCode.timesheetCode}>{timesheetCode.timesheetCode} - {timesheetCode.timesheetCodeDesc}</option>
                                                            ))}
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Estimated Start Date</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input type="date" name="estimatedStartDate" value = {this.state.job.estimatedStartDate} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Estimated End Date</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input type="date" name="estimatedEndDate" value = {this.state.job.estimatedEndDate} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Parent Job ( Project )</label>
                                                    </td>
                                                    <td colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <select name="parentJobNo" value = {this.state.job.parentJobNo} onChange={this.handleChange}>
                                                            <option value=""></option>
                                                            {this.state.parentJobList.map((parentJob) => (
                                                                <option key={parentJob.jobNo} value={parentJob.jobNo}>{parentJob.jobName} - {parentJob.jobNo}</option>
                                                            ))}
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Active</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input type="checkbox" name="active" checked={this.state.job.active} onChange={this.handleChangeCheckBox} />
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

export default withRouter(JobEdit);