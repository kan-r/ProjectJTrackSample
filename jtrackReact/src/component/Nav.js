import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import {Navbar, Nav, NavDropdown} from 'react-bootstrap';
import AuthService from '../service/AuthService';

class Navmenu extends Component {

    handleSelect(key){
        if(key === '/Logout'){
            AuthService.logout();
            this.props.updateAppUser('');
            this.props.history.push("/Login?logout=true");
        }
    }

    render() {
        return (
            <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
            <Navbar.Toggle aria-controls="responsive-navbar-nav" />
            <Navbar.Collapse id="responsive-navbar-nav">
                <Nav className="mr-auto" activeKey={window.location.pathname}>
                    <Nav.Link href="Job">Job</Nav.Link>
                    <Nav.Link href="Timesheet">Timesheet</Nav.Link>
                    <NavDropdown title="Admin" id="collasible-nav-dropdown">
                        <NavDropdown.Item href="JobType">Job Type</NavDropdown.Item>
                        <NavDropdown.Item href="JobStatus">Job Status</NavDropdown.Item>
                        <NavDropdown.Item href="JobPriority">Job Priority</NavDropdown.Item>
                        <NavDropdown.Item href="JobResolution">Job Resolution</NavDropdown.Item>
                        <NavDropdown.Item href="JobStage">Job Stage</NavDropdown.Item>
                        <NavDropdown.Item href="TimesheetCode">Timesheet Code</NavDropdown.Item>
                        <NavDropdown.Item href="User">User</NavDropdown.Item>
                    </NavDropdown>
                </Nav>
                <Nav onSelect={key =>this.handleSelect(key)} activeKey={window.location.pathname}>
                    <Nav.Link disabled>
                        <i>JTrack</i> Welcome {this.props.appUser}
                    </Nav.Link>
                    <Nav.Link eventKey="/Logout">Log out</Nav.Link>
                </Nav>
            </Navbar.Collapse>
            </Navbar>
        );
    }
}

export default withRouter(Navmenu);