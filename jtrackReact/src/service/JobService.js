import axios from 'axios';
import AuthService from './AuthService';

const BASE_URL = AuthService.getBaseUrl();

class JobService {

    getJobList(){
        return axios.get(BASE_URL + '/jobs', AuthService.getHttpOptions());
    }

    getJobList2(jobSO){
        let url = `/jobs?name=${jobSO.jobName}&type=${jobSO.jobType}&status=${jobSO.jobStatus}&assignedTo=${jobSO.assignedTo}&includeChild=${jobSO.includeChildJobs}&nameC=${jobSO.jobNameChild}&typeC=${jobSO.jobTypeChild}&statusC=${jobSO.jobStatusChild}&assignedToC=${jobSO.assignedToChild}`;
        return axios.get(BASE_URL + url, AuthService.getHttpOptions());
    }

    getParentJobList(){
        return axios.get(BASE_URL + '/jobs?type=Project', AuthService.getHttpOptions());
    }

    getJob(jobNo){
        return axios.get(BASE_URL + `/jobs/${jobNo}`, AuthService.getHttpOptions());
    }

    addJob(job){
        job.userCrt = AuthService.getAppUser();
        return axios.post(BASE_URL + '/jobs', job, AuthService.getHttpOptions());
    }

    updateJob(job){
        job.userMod = AuthService.getAppUser();
        return axios.put(BASE_URL + `/jobs/${job.jobNo}`, job, AuthService.getHttpOptions());
    }

    deleteJob(jobNo){
        return axios.delete(BASE_URL + `/jobs/${jobNo}`, AuthService.getHttpOptions());
    }
}

export default new JobService();