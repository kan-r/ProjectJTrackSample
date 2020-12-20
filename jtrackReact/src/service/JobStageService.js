import axios from 'axios';
import AuthService from './AuthService';

const BASE_URL = AuthService.getBaseUrl();

class JobStageService {

    getJobStageList(){
        return axios.get(BASE_URL + '/jobStages', AuthService.getHttpOptions());
    }

    getJobStage(jobStage){
        return axios.get(BASE_URL + `/jobStages/${jobStage}`, AuthService.getHttpOptions());
    }

    addJobStage(jobStage){
        jobStage.userCrt = AuthService.getAppUser();
        return axios.post(BASE_URL + '/jobStages', jobStage, AuthService.getHttpOptions());
    }

    updateJobStage(jobStage){
        jobStage.userMod = AuthService.getAppUser();
        return axios.put(BASE_URL + `/jobStages/${jobStage.jobStage}`, jobStage, AuthService.getHttpOptions());
    }

    deleteJobStage(jobStage){
        return axios.delete(BASE_URL + `/jobStages/${jobStage}`, AuthService.getHttpOptions());
    }
}

export default new JobStageService();