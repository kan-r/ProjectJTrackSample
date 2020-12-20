import axios from 'axios';
import AuthService from './AuthService';

const BASE_URL = AuthService.getBaseUrl();

class JobResolutionService {

    getJobResolutionList(){
        return axios.get(BASE_URL + '/jobResolutions', AuthService.getHttpOptions());
    }

    getJobResolution(jobResolution){
        return axios.get(BASE_URL + `/jobResolutions/${jobResolution}`, AuthService.getHttpOptions());
    }

    addJobResolution(jobResolution){
        jobResolution.userCrt = AuthService.getAppUser();
        return axios.post(BASE_URL + '/jobResolutions', jobResolution, AuthService.getHttpOptions());
    }

    updateJobResolution(jobResolution){
        jobResolution.userMod = AuthService.getAppUser();
        return axios.put(BASE_URL + `/jobResolutions/${jobResolution.jobResolution}`, jobResolution, AuthService.getHttpOptions());
    }

    deleteJobResolution(jobResolution){
        return axios.delete(BASE_URL + `/jobResolutions/${jobResolution}`, AuthService.getHttpOptions());
    }
}

export default new JobResolutionService();