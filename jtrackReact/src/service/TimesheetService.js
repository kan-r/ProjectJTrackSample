import axios from 'axios';
import AuthService from './AuthService';

const BASE_URL = AuthService.getBaseUrl();

class TimesheetService {

    getTimesheetList(){
        return axios.get(BASE_URL + '/timesheets', AuthService.getHttpOptions());
    }

    getTimesheetList2(timesheetSO){
        let url = `/timesheets?userId=${timesheetSO.userId}&workedDateFrom=${timesheetSO.workedDateFrom}&workedDateTo=${timesheetSO.workedDateTo}`;
        return axios.get(BASE_URL + url, AuthService.getHttpOptions());
    }

    getTimesheet(timesheetId){
        return axios.get(BASE_URL + `/timesheets/${timesheetId}`, AuthService.getHttpOptions());
    }

    addTimesheet(timesheet){
        timesheet.userCrt = AuthService.getAppUser();
        return axios.post(BASE_URL + '/timesheets', timesheet, AuthService.getHttpOptions());
    }

    updateTimesheet(timesheet){
        timesheet.userMod = AuthService.getAppUser();
        return axios.put(BASE_URL + `/timesheets/${timesheet.timesheetId}`, timesheet, AuthService.getHttpOptions());
    }

    deleteTimesheet(timesheetId){
        return axios.delete(BASE_URL + `/timesheets/${timesheetId}`, AuthService.getHttpOptions());
    }
}

export default new TimesheetService();