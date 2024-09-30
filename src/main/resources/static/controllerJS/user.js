window.addEventListener('load', () => {

    loggedUserPrivilege = ajaxGetRequest("/privilege/bymodule/USER");

    console.log(loggedUserPrivilege);

    refreshUserTable();

    refreshUserform();

})

//define fn for refresh table when browser reloads
const refreshUserTable = () => {   //

    //define array for store data
    users = [];

    //call jquery ajax fn
    $.ajax("/user/alldata", {
        type: "GET",
        contentType: "application/json",
        async: false,    //true dammoth data enakam balan inne na
        success: function (data) {
            console.log("success" + data);
            users = data;
        },
        error: function (resData) {
            console.log("fail " + resData);
            users = []
        }
    });

    const displayPropertyList = [
        { dataType: "function", propertyName: getEmployeeFullname },
        { dataType: "text", propertyName: "username" },
        { dataType: "text", propertyName: "email" },
        { dataType: "function", propertyName: getRoles },
        { dataType: "function", propertyName: getUserStatus },     //data type eka function nm "" one na


    ];
    fillDataIntoTable3(tableUser, users, displayPropertyList, buttonVisibility = true, loggedUserPrivilege);

    //call the new datatable format(from net)
    $('#tableUser').dataTable();

}

//fn for refresh user form after a task/after each reload
const refreshUserform = () => {

    //create variable user as new object
    user = new Object();
    oldUser = null; //???? retype pw eke error check karaddis   

    user.roles = new Array();

    userForm.reset();

    //get data list for fill dynamic select element
    empListWOUserAcc = ajaxGetRequest("/emp/listwithoutuseracc");
    fillDataIntoSelect2(selectEmployee, "Select Employee", empListWOUserAcc, "nic", "fullname")

    rolesList = ajaxGetRequest("/role/roleswoadmin");
    flushCollapseUserRoles.innerHTML = "";
    rolesList.forEach(element => {

        let newDiv = document.createElement('div');
        newDiv.className = "form-check form-check-inline";
        newDiv.style.minWidth= "200px"

        let newInput = document.createElement('input');
        newInput.classList.add("form-check-input");
        newInput.type = "checkbox";
        newInput.setAttribute('id', JSON.stringify(element.name))

        let newLabel = document.createElement('label');
        newLabel.classList.add("form-check-label");
        newLabel.innerText = element.name;
        
        newLabel.setAttribute('for', JSON.stringify(element.name))

        newInput.onchange = function () {
            if (this.checked) {
                user.roles.push(element)
            } else {

                user.roles.pop(element)

                let existIndex = user.roles.map(role => role.id).indexOf(element.id);
                if (existIndex != -1) {
                    user.roles.splice(existIndex, 1)
                }
            }
        }

        newDiv.appendChild(newInput);
        newDiv.appendChild(newLabel);

        flushCollapseUserRoles.appendChild(newDiv)

    });


    user.status = false;

    // usreAccStatus.checked = false;
    // usreAccLabel.innerText = "User Account Is Not Active";

    selectEmployee.style.border = "1px solid #ced4da";
    inputUserName.style.border = "1px solid #ced4da";
    inputPwd.style.border = "1px solid #ced4da";
    rePwd.style.border = "1px solid #ced4da";
    inputEmail.style.border = "1px solid #ced4da";

    userUpdtBtn.disabled = true;
    userUpdtBtn.style.cursor = "not-allowed";

    if (loggedUserPrivilege.privinsert) {
        userAddBtn.disabled = false;
        userAddBtn.style.cursor = "pointer"
    } else {
        userAddBtn.disabled = true;
        userAddBtn.style.cursor = "not-allowed"
    }

}

//get emp full name to display on table
const getEmployeeFullname = (ob) => {
    return ob.employee_id.fullname;
}

//
const getRoles = (ob) => {

    let roles = '';  //me refresh user form eke hadapu array ekamada ?
    for (const index in ob.roles) {
        if (index == ob.roles.length - 1) {
            roles = roles + ob.roles[index].name;
        } else {
            roles += ob.roles[index].name + ' ,';
            //roles = roles + ob.roles[index].name + ", ";
        }
    }

    return roles;

    //or

    // let userRoles = '';
    // ob.roles.forEach((element, index) => {
    //     if (ob.roles.length - 1 == index) {
    //         userRoles = userRoles + element.name;
    //     }
    //     else {
    //         userRoles = userRoles + element.name + ", ";
    //     }
    // });

    // return userRoles;

    //or

    // let roles = "";
    // for (const index in ob.roles) {
    //     //roles = roles + ob.roles[index].name + (index == ob.roles.length-1) ? " ": ", "; <-- menn mehem thani line eken palleha tika liyann puluwn (eth mek hariytm hari naa poddk search krl blnnd)
    //     if (index == ob.roles.length - 1) {
    //         roles = roles + ob.roles[index].name;
    //     } else {
    //         roles = roles + ob.roles[index].name + ", ";
    //     }
    // }
    // return roles;
}

//
const getUserStatus = (ob) => {
    if (ob.status) {
        return '✅ '

    } else {
        return '🟥 '
    }
}

//fn for bind and validate the retype pw field
const retypePasswordVali = () => {
    if (rePwd.value == inputPwd.value) {
        rePwd.style.border = "2px solid green";
        user.password = rePwd.value;
    } else {
        rePwd.style.border = '2px solid red';
        user.password = null;
    }
}

//fn for support "addUser" fn
const checkUserErrors = () => {
    let errors = '';
    // just let errors; kiyala dammoth undefined kiyala thamay check karanna wenne

    if (user.employee_id == null) {
        errors = errors + "Employee name can't be empty \n";

    }
    if (user.username == null) {
        errors = errors + "Username can't be empty \n";

    }
    if (user.password == null) {
        errors = errors + "password can't be empty \n";

    }
    //bind wenn field ekk naa ek nisa html eken tag id ek arnn tmi check krnn ekai ek null nathuw emptyd kiyl blnn
    // if (rePwd.value == inputPwd.value) {
    //     errors = errors + "re-password can't be empty \n";

    // }
    if (user.email == null) {
        errors = errors + "Email can't be empty \n";

    }
    if (user.roles.length == 0) {
        errors = errors + "You must select at least one Role \n";

    }

    return errors;
}

//fn for check changed values
const checkUserUpdates = () => {
    let updates = '';

    if (oldUser.inputUserName != user.inputUserName) {
        updates = updates + oldUser.inputUserName + " has changed into " + user.inputUserName;
    }

    if (oldUser.email != user.email) {
        updates = updates + oldUser.email + " has changed into " + user.email;
    }

    if (user.roles.length != oldUser.roles.length) {
        alert("Role Has Changed");
    } else {
        for (let element of user.roles) {
            let existRoleCount = oldUser.roles.map(item => item.id).indexOf(element.id);

            if (existRoleCount == -1) {
                updates = updates + "role has changed";
                break;
            }
        }
    }

    if (user.status !== oldUser.status) {
        updates = updates + "Status is change to " + user.status;
    }
    //explain this 💥 ?????
    if (user.roles !== oldUser.role) {
        updates = updates + "role is change to " + user.role;
    }


    return updates;
}

//3 MODAL BTNS
//fn for support UPDATE btn
const refillUserForm = (ob) => {

    console.log('edit btn clkd');

    user = JSON.parse(JSON.stringify(ob));
    oldUser = JSON.parse(JSON.stringify(ob));

    $("#userModal").modal("show");

    // get data list for fill dynamic select element
    empListWOUserAcc = ajaxGetRequest("/emp/listwithoutuseracc");
    empListWOUserAcc.push(user.employee_id);

    fillDataIntoSelect2(selectEmployee, "Select Employee", empListWOUserAcc, "nic", "fullname", user.employee_id.fullname);

    inputUserName.disabled = true;

    rolesList = ajaxGetRequest("/role/roleswoadmin");
    flushCollapseUserRoles.innerHTML = "";
    rolesList.forEach(element => {

        let newDiv = document.createElement('div');
        newDiv.className = "form-check form-check-inline";
        // newDiv.style.width = "30%";
        newDiv.style.minWidth= "200px"
        
        let newInput = document.createElement('input');
        newInput.classList.add("form-check-input");
        newInput.type = "checkbox";
        newInput.setAttribute('id', JSON.stringify(element.name))

        let newLabel = document.createElement('label');
        newLabel.classList.add("form-check-label");
        newLabel.innerText = element.name;
       
        newLabel.setAttribute('for', JSON.stringify(element.name))

        newInput.onchange = function () {
            if (this.checked) {
                user.roles.push(element)
            } else {

                user.roles.pop(element)

                let existIndex = user.roles.map(role => role.id).indexOf(element.id);
                if (existIndex != -1) {
                    user.roles.splice(existIndex, 1)
                }
            }
        }

        let existIndex = user.roles.map(role => role.id).indexOf(element.id);
        if (existIndex != -1) {
            newInput.checked = true;
        }

        newDiv.appendChild(newInput);
        newDiv.appendChild(newLabel);

        flushCollapseUserRoles.appendChild(newDiv)

    });

    if (user.status) {
        userAccActive.checked = true;
        // usreAccLabel.innerText = "User Account Is Active";
    } else {
        userAccInactive.checked = false;
        // usreAccLabel.innerText = "User Account Is Not Active";
    }

    inputUserName.value = user.username;
    inputEmail.value = user.email;

    // password fields 2th disabled karanna


    userAddBtn.disabled = true;
    userAddBtn.style.cursor = "not-allowed";

    userUpdtBtn.disabled = false;
    userUpdtBtn.style.cursor = "pointer";

    //    if (privileges.privupdate) {
    //        userUpdtBtn.disabled = false;
    //        userUpdtBtn.style.cursor="pointer"
    //    } else {
    //        userUpdtBtn.disabled = true;
    //        userUpdtBtn.style.cursor="not-allowed" 
    //    }

}

//fn for delete a record
const deleteUser = (ob) => {
    console.log('delete button clicked');

    const userConfirm = confirm('Delete following account ? \n ' + ob.username);

    if (userConfirm) {
        let deleteServerResponse = ajaxRequest("/user", "DELETE", ob);
        if (deleteServerResponse == "OK") {
            alert('Deleted succesfully')
            refreshUserTable();
        } else {
            alert("Delete Failed \n" + deleteServerResponse);
        }
    }
}

//fn for print a record
const printUser = () => { }


//3 FORM BTNS w/o clear

//add a record ** ADD btn
const userAdd = () => {
    console.log('add btn clkd');

    let errors = checkUserErrors();
    if (errors == '') {
        const userConfirm = confirm('are you sure to add ?');

        if (userConfirm) {
            let postServerResponse = ajaxRequest("/user", "POST", user);
            if (postServerResponse == 'OK') {
                alert('saved successfully');
                refreshUserTable();
                userForm.reset();
                refreshUserform();

            } else {
                alert('failed to submit ' + postServerResponse);
            }
        } else {
            alert('user cancelled the task');
        }
    } else {
        alert('form has some errors ' + errors);
    }
}

//fn for UPDATE btn
const userUpdate = () => {
   
    console.log(oldUser);
    console.log(user);

    let errors = checkUserErrors();
    if (errors == '') {
        let updates = checkUserUpdates();
        if (updates == '') {
            alert('nothing to update')
        } else {
            let userResponse = confirm('are you sure to update ?');
            if (userResponse) {
                let putServiceResponse = ajaxRequest('/user', 'PUT', user);
                if (putServiceResponse == 'OK') {
                    alert('successfully updated');
                    $('#userModal').modal("hide");
                    refreshUserTable();
                    userForm.reset();
                    refreshUserform();

                } else {
                    alert("An error occured \n" + putServiceResponse);
                }
            } else {
                alert("Operation cancelled by the Operator");

            }
        }
    } else {
        alert('form has following errors ' + errors)
    }
}

// auto generate the email
const generateEmail = () => {
    inputEmail.value = JSON.parse(selectEmployee.value).email;  //set value
    user.email = inputEmail.value; //bind value
    inputEmail.style.border = "2px solid green";
}


