import axios from 'axios';
import AuthService from './AuthService';

const BASE_URL = AuthService.getBaseUrl();

class JobTypeService {

    getJobTypeList(){
        return axios.get(BASE_URL + '/jobTypes', AuthService.getHttpOptions());
    }

    getJobType(jobType){
        return axios.get(BASE_URL + `/jobTypes/${jobType}`, AuthService.getHttpOptions());
    }

    addJobType(jobType){
        jobType.userCrt = AuthService.getAppUser();
        return axios.post(BASE_URL + '/jobTypes', jobType, AuthService.getHttpOptions());
    }

    updateJobType(jobType){
        jobType.userMod = AuthService.getAppUser();
        return axios.put(BASE_URL + `/jobTypes/${jobType.jobType}`, jobType, AuthService.getHttpOptions());
    }

    deleteJobType(jobType){
        return axios.delete(BASE_URL + `/jobTypes/${jobType}`, AuthService.getHttpOptions());
    }
}

export default new JobTypeService();