import axios from 'axios';
import AuthService from './AuthService';

const BASE_URL = AuthService.getBaseUrl();

class JobPriorityService {

    getJobPriorityList(){
        return axios.get(BASE_URL + '/jobPriorities', AuthService.getHttpOptions());
    }

    getJobPriority(jobPriority){
        return axios.get(BASE_URL + `/jobPriorities/${jobPriority}`, AuthService.getHttpOptions());
    }

    addJobPriority(jobPriority){
        jobPriority.userCrt = AuthService.getAppUser();
        return axios.post(BASE_URL + '/jobPriorities', jobPriority, AuthService.getHttpOptions());
    }

    updateJobPriority(jobPriority){
        jobPriority.userMod = AuthService.getAppUser();
        return axios.put(BASE_URL + `/jobPriorities/${jobPriority.jobPriority}`, jobPriority, AuthService.getHttpOptions());
    }

    deleteJobPriority(jobPriority){
        return axios.delete(BASE_URL + `/jobPriorities/${jobPriority}`, AuthService.getHttpOptions());
    }
}

export default new JobPriorityService();