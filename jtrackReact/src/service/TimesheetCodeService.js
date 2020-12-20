import axios from 'axios';
import AuthService from './AuthService';

const BASE_URL = AuthService.getBaseUrl();

class TimesheetCodeService {

    getTimesheetCodeList(){
        return axios.get(BASE_URL + '/timesheetCodes', AuthService.getHttpOptions());
    }

    getTimesheetCode(timesheetCode){
        return axios.get(BASE_URL + `/timesheetCodes/${timesheetCode}`, AuthService.getHttpOptions());
    }

    addTimesheetCode(timesheetCode){
        timesheetCode.userCrt = AuthService.getAppUser();
        return axios.post(BASE_URL + '/timesheetCodes', timesheetCode, AuthService.getHttpOptions());
    }

    updateTimesheetCode(timesheetCode){
        timesheetCode.userMod = AuthService.getAppUser();
        return axios.put(BASE_URL + `/timesheetCodes/${timesheetCode.timesheetCode}`, timesheetCode, AuthService.getHttpOptions());
    }

    deleteTimesheetCode(timesheetCode){
        return axios.delete(BASE_URL + `/timesheetCodes/${timesheetCode}`, AuthService.getHttpOptions());
    }
}

export default new TimesheetCodeService();