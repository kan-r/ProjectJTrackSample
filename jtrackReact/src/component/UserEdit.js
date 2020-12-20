import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Link } from 'react-router-dom';
import UserService from '../service/UserService';
import Message from './Message';

class UserEdit extends Component {

    constructor(props) {
        super(props)
        this.state = {
            user: {
                userId: '',
                pword: '',
                firstName: '',
                lastName: '',
                active: true,
                userMod: ''
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
        const { match: { params } } = this.props;
        let userId = params.userId;
        this.getUser(userId);
    }

    getUser(userId){
        UserService.getUser(userId)
        .then(res => {
            this.updateMessage('');
            this.setState({ user: res.data })
        })
        .catch(err => {
            this.updateMessage(err, true);
        });;
    }

    handleChange(event) {
       
        let user = {
            ...this.state.user,
            [event.target.name] : event.target.value
        };

        this.setState({user:user});
    }

    handleChangeCheckBox(event) {
        
        let user = {
            ...this.state.user,
            active : !this.state.user.active
        };

        this.setState({user:user});
    }

    handleSubmit(event) {
        event.preventDefault();

        let pword  = this.state.user.pword;
        if(pword == null || pword.trim() === ''){
            this.updateMessage('Password is required', true);
            return;
        }

        UserService.updateUser(this.state.user)
        .then(res => {
            this.updateMessage('');
            try{
                this.props.history.push(`/User`);
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
                        <Link to='/User' className="button">Cancel</Link>
                        <input type="submit" value="Save" className="button" />
                    </div>
                    <div className="form-region-2">
                        <table cellPadding="0" border="0" cellSpacing="0" summary="" className="form-standard">
                            <thead>
                                <tr>
                                    <th><div>UserEdit User</div></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <table border="0" summary="" >
                                            <tbody>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>User ID</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="userId" value={this.state.user.userId} onChange={this.handleChange} disabled={true} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Password</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="pword" value={this.state.user.pword} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>First Name</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="firstName" value={this.state.user.firstName} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Last Name</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input name="lastName" value={this.state.user.lastName} onChange={this.handleChange} />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td nowrap="nowrap" align="right">
                                                        <label>Active</label>
                                                    </td>
                                                    <td  colSpan="1" rowSpan="1" align="left" valign="middle">
                                                        <input type="checkbox" name="active" checked={this.state.user.active} onChange={this.handleChangeCheckBox} />
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

export default withRouter(UserEdit);