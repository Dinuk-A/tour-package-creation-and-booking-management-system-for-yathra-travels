window.addEventListener('load', () => {

    //user anuwa privileges js ekata gennanawa
    loggedUserPrivileges = ajaxGetRequest("/privilege/bymodule/EMPLOYEE");
    console.log(loggedUserPrivileges);

    refreshEmployeeTable();

    refreshEmployeeForm();
});

//to refresh main tabke
const refreshEmployeeTable = () => {

    employees = ajaxGetRequest("/emp/alldata");

    //💥NORMAL WAY OF AJAX REQS💥
    // employees = [];
    // $.ajax("/emp/alldata", {
    //     type: "GET",
    //     contentType: "json",
    //     async: false,

    //     success: function (data) {
    //         console.log("success");
    //         employees = data;
    //     },

    //     error: function (resOb) {
    //         console.log("fail");
    //         employees = [];
    //     }
    // });

    const displayProperty =
        [
            { dataType: 'text', propertyName: 'fullname' },
            { dataType: 'text', propertyName: 'nic' },
            { dataType: 'text', propertyName: 'email' },
            { dataType: 'imgArray', propertyName: 'emp_photo' },
            { dataType: 'text', propertyName: 'mobile' },
            { dataType: 'function', propertyName: getDesignation },
            { dataType: 'function', propertyName: getEmployeeStatus },
            // { dataType: 'function', propertyName: getHasUserAcc }

        ]
    fillDataIntoTable3(empMainTable, employees, displayProperty, buttonVisibility = true, disableButtonsCommonFn) //loggedUserPrivileges


    $('#empMainTable').dataTable();
}

const disableButtonsCommonFn = (rowOb) => {

    if (!loggedUserPrivileges.privupdate) {
        btnEdit.disabled = true;
        btnEdit.style.cursor = "not-allowed"
    }

    if (!loggedUserPrivileges.privdelete) {
        btnDelete.disabled = true;
        btnDelete.style.cursor = "not-allowed"
    } else {
        if (rowOb.employeestatus_id.name == "Deleted") {
            btnDelete.disabled = true;
            btnDelete.style.cursor = "not-allowed";
        }
    }
    if (!loggedUserPrivileges.privselect) {
        btnPrint.disabled = true;
        btnPrint.style.cursor = "not-allowed"
    }
}

//FOR FILL THE TABLE
const getDesignation = (ob) => {
    return ob.designation_id.name;
}

//FOR FILL THE TABLE
const getEmployeeStatus = (ob) => {
    return ob.employeestatus_id.name;

}

//FOR FILL THE TABLE
// const getHasUserAcc = (ob) => {
//     if (ob.getHasUserAcc) {
//         return "HAS";
//     } else {
//         return 'NO';
//     }
// }

//CUSTOM FN TO VALIDATE AND BIND THE FULL NAME
const fullNameValidator = (fieldId) => {

    const pattern = '^([A-Z][a-z]{2,20}[\\s])+([A-Z][a-z]{2,20})$';
    const regPattern = new RegExp(pattern);

    if (regPattern.test(fieldId.value)) {
        //VALID
        fieldId.style.border = '3px solid green';
        employee.fullname = fieldId.value;

    } else {
        //invalid
        fieldId.style.border = '2px solid red'
        employee.fullname = null;
    }

}

//FOR MAIN ADD BUTTON
const btnAddEmp = () => {

    //check form error
    const errors = checkFormErrors();

    if (errors == '') {

        const userConfirm = confirm("Are You Sure To Add ?\n " + employee.fullname);

        if (userConfirm) {
            //call POST service 
            let postServiceReqResponse;

            //jquery ajax ("url" , {option})
            $.ajax("/emp", {
                type: "POST",
                data: JSON.stringify(employee),
                contentType: "application/json",
                async: false,
                success: function (data) {
                    console.log("success" + data);
                    postServiceReqResponse = data;
                },
                error: function (responseObj) {
                    console.log("fail" + responseObj);
                    postServiceReqResponse = responseObj;
                }
            });
            if (postServiceReqResponse == "OK") {
                
                alert('Succesfully Saved !!!')
                refreshEmployeeTable();
                formEmployee.reset();
                refreshEmployeeForm();
                $("#modalEmployeeAdd").modal("hide");


            } else {
                alert("post service failed \n " + postServiceReqResponse)

            }
        }

    } else {
        alert('form has following errors... \n \n' + errors)
    }

}

//FOR REFRESH MAIN FORM
const refreshEmployeeForm = () => {

    employee = new Object();

    formEmployee.reset();

    designations = ajaxGetRequest("/des/alldata")
    fillDataIntoSelect(selectDesignation, 'Select Designation', designations, 'name')

    employeeStatuses = ajaxGetRequest("/status/alldata")
    fillDataIntoSelect(selectEmployeementStatus, 'Select Employee Status', employeeStatuses, 'name')

    inputFullName.style.border = "1px solid #ced4da";
    inputNIC.style.border = "1px solid #ced4da";
    dateDateOfBirth.style.border = "1px solid #ced4da";
    inputEmail.style.border = "1px solid #ced4da";
    inputMobile.style.border = "1px solid #ced4da";
    inputLand.style.border = "1px solid #ced4da";
    inputAddress.style.border = "1px solid #ced4da";
    inputNote.style.border = "1px solid #ced4da";
    selectEmployeementStatus.style.border = "1px solid #ced4da";
    selectDesignation.style.border = "1px solid #ced4da";
    selectMartialStatus.style.border = "1px solid #ced4da";

    refreshEmployeeTable();

    //initially UPDATE button should be disabled (when in ADD mode)
    empUpdateBtn.disabled = true;
    empUpdateBtn.style.cursor = "not-allowed";

    // empUpdateBtn.style.visibility = "hidden"
    //mekath hari >> empUpdateBtn.disabled = "";

}

//REFILL FORM WHEN EDIT BUTTON CLICKED
const empolyeeFormReFill = (ob) => {

    $('#modalEmployeeAdd').modal('show');

    //assign table row into employee object
    employee = JSON.parse(JSON.stringify(ob));    //mulin string karala, aye json walata convert karanawa, ethakota denna ekama ob ekak wenne na, compare karanna one nisa
    oldEmployee = JSON.parse(JSON.stringify(ob));

    if (employee.emp_photo != null) {
        empImg.src = atob(employee.emp_photo);
    } else {
        empImg.src = 'resources/images/sigiriya.jpg';
    }

    inputFullName.value = employee.fullname;
    inputNIC.value = employee.nic;
    inputEmail.value = employee.email;
    inputMobile.value = employee.mobile;
    inputLand.value = employee.landno;
    inputAddress.value = employee.address;
    inputNote.value = employee.note;
    dateDateOfBirth.value = employee.dob;
    selectMartialStatus.value = employee.martialstatus;

    if (employee.gender == "Male") {
        radioMale.checked = true;
    } else {
        radioFemale.checked = true;
    }

    designations = ajaxGetRequest("/des/alldata");
    fillDataIntoSelect(selectDesignation, 'select designation', designations, 'name', employee.designation_id.name);

    employeeStatuses = ajaxGetRequest("/status/alldata");
    fillDataIntoSelect(selectEmployeementStatus, 'select status', employeeStatuses, 'name', employee.employeestatus_id.name);

    //add button should be disabled in EDIT MODE
    empAddBtn.disabled = true;
    empAddBtn.style.cursor = "not-allowed";

    //formrefresh ekedi disabled karapu btn eka methanin enable karanawa
    empUpdateBtn.disabled = false;
    empUpdateBtn.style.cursor = "pointer";

    //MEKA CHECK KARANNA NAM KATAHARI UPDATE PRV AYN KARALA BALANNA💥💥💥

    //disabling update button based on USER PRIVILEGES 
    // if (loggedUserPrivileges.privupdate) {
    //     empUpdateBtn.disabled = false;
    // } else {
    //     empUpdateBtn.disabled = true;
    // }

}

//FOR DELETE BUTTON
const deleteEmployee = (ob, rowIndex) => {

    empMainTable.children[1].children[rowIndex].style.backgroundColor = 'red';

    setTimeout(function () {
        const userConfirm = confirm('Are you sure to delete following Employee \n '
            + '\n Full Name is ' + ob.fullname
            + '\n NIC is ' + ob.nic
            + '\n Status is ' + ob.employeestatus_id.name
        );

        if (userConfirm) {
            // call delete services 
            let deleteServerResponce;
            $.ajax("/emp", {
                type: "DELETE",
                async: false,
                contentType: "application/json",
                data: JSON.stringify(ob),
                success: function (data) {
                    console.log("success" + data);
                    deleteServerResponce = data;
                },
                error: function (resOb) {
                    console.log("failed : " + resOb);
                    deleteServerResponce
                }
            }
            );

            if (deleteServerResponce == 'OK') {
                alert('Delete Successfully...!');
                refreshEmployeeTable();

            } else {
                alert('Delete Not Completed. You have following error \n' + deleteServerResponce);
            }
        } else {
            empMainTable.children[1].children[rowIndex].style.backgroundColor = 'green';
        }
    }, 500);
}

//define a fn for Update button in form
const btnUpdateEmpForm = () => {

    let errors = checkFormErrors();
    if (errors == "") {
        let updates = showUpdatedValues();
        if (updates == "") {
            alert("No changes detected");
        } else {
            let userConfirm = confirm("Are you sure to proceed ? \n \n" + updates);

            if (userConfirm) {
                let putServiceResponse;

                $.ajax("/emp", {
                    type: "PUT",
                    async: false,
                    contentType: "application/json",
                    data: JSON.stringify(employee),
                    success: function (data) {
                        putServiceResponse = data;
                    },
                    error: function (resData) {
                        putServiceResponse = resData;
                    }
                });

                if (putServiceResponse == "OK") {
                    alert("Successfully Updated");
                    $("#modalEmployeeAdd").modal("hide");
                    refreshEmployeeTable();
                    formEmployee.reset();
                    refreshEmployeeForm()

                } else {
                    alert("An error occured \n" + putServiceResponse);
                }
            } else {
                alert("Operation cancelled by the Operator");
            }
        }


    } else {
        alert("There is an unfilled field \n" + errors);
    }
}

//create a fn for display changed values as an alert
const showUpdatedValues = () => {
    let updates = "";

    if (employee.fullname != oldEmployee.fullname) {
        updates = updates + oldEmployee.fullname + " will be changed to " + employee.fullname + "\n";
    }
    if (employee.nic != oldEmployee.nic) {
        updates = updates + oldEmployee.nic + " will be changed to " + employee.nic + "\n";
    }
    if (employee.mobile != oldEmployee.mobile) {
        updates = updates + oldEmployee.mobile + " will be changed to " + employee.mobile + "\n";
    }
    if (employee.dob != oldEmployee.dob) {
        updates = updates + oldEmployee.dob + " will be changed to " + employee.dob + "\n";
    }
    if (employee.email != oldEmployee.email) {
        updates = updates + oldEmployee.email + " will be changed to " + employee.email + "\n";
    }
    if (employee.address != oldEmployee.address) {
        updates = updates + oldEmployee.address + " will be changed to " + employee.address + "\n";
    }
    if (employee.martialstatus != oldEmployee.martialstatus) {
        updates = updates + oldEmployee.martialstatus + " will be changed into " + employee.martialstatus + "\n";
    }
    if (employee.designation_id.name != oldEmployee.designation_id.name) {
        updates = updates + oldEmployee.designation_id.name + " will be changed into " + employee.designation_id.name + "\n"
    }
    if (employee.employeestatus_id.name != oldEmployee.employeestatus_id.name) {
        updates = updates + oldEmployee.employeestatus_id.name + " will be changed into " + employee.employeestatus_id.name + "\n"
    }

    // if (employee.emp_photo_name != oldEmployee.emp_photo_name) {
    //     updates = updates + " Photo has changed";
    // }

    if (employee.emp_photo != oldEmployee.emp_photo) {
        updates = updates + " Photo has changed";
    }

    return updates;
}

//fn for check all the required fields are filled or not
const checkFormErrors = () => {
    let errors = "";

    if (employee.fullname == null) {
        errors = errors + "Full Name cannot be empty \n";
    }

    if (employee.nic == null) {
        errors = errors + "NIC cannot be empty \n";
    }

    if (employee.dob == null) {
        errors = errors + "DOB cannot be empty \n";
    }

    if (employee.gender == null) {
        errors = errors + "Gender cannot be empty \n";
    }

    if (employee.martialstatus == null) {
        errors = errors + "Civil Status cannot be empty \n";
    }

    if (employee.email == null) {
        errors = errors + "Email cannot be empty \n";
    }

    if (employee.mobile == null) {
        errors = errors + "Mobile Number cannot be empty \n"
    }

    if (employee.address == null) {
        errors = errors + "Address cannot be empty \n"
    }

    if (employee.designation_id == null) {
        errors = errors + "Designation cannot be empty \n"
    }

    if (employee.employeestatus_id == null) {
        errors = errors + "Employee Status cannot be empty \n"
    }

    return errors;
}

const printEmployee = (rowOb, rowIndex) => {

    //option one
    let addedUser = ajaxGetRequest("/username/byid/" + rowOb.addeduserid);

    tdEmpName.innerHTML = rowOb.fullname;
    tdAddedTime.innerHTML = rowOb.addeddatetime.split("T")[0] + " " + rowOb.addeddatetime.split('T')[1];
    tdAddedUser.innerHTML = addedUser.username;

    // let newWindow = window.open();
    //    newWindow.document.write("<h1>" + rowOb.fullname + "details </h1>");
    // newWindow.document.write("<html> <title> print example</title> <body> <h1> print example table test </h1>" + toPrinttable.outerHTML + "</body> </html>");

    //udin new tab eke data load wena window eka write wela iwara wenna timeout ekak dunna.
    /* setTimeout(() => {
         newWindow.stop();  //load wena eka stop karanawa
         newWindow.print();   //eke print option eka call karanawa
         newWindow.close();  //then close after click cancel button
     }, 1000);
 */

    //option 2
    $('#printExModal').modal('show');






}

const btnprint = () => {
    let newWindow = window.open();
    //    newWindow.document.write("<h1>" + rowOb.fullname + "details </h1>");
    newWindow.document.write("<html> <title> print example</title> <body> <h1> print example table test </h1>" + toPrinttable.outerHTML + "</body> </html>");

    //udin new tab eke data load wena window eka write wela iwara wenna timeout ekak dunna.
    setTimeout(() => {
        newWindow.stop();  //load wena eka stop karanawa
        newWindow.print();   //eke print option eka call karanawa
        newWindow.close();  //then close after click cancel button
    }, 1000);
}

const imgValidatorForEmpJs = (fileElement, object, imgProperty, imgNameProperty, previewId) => {

    if (fileElement.files != null) {
        console.log(fileElement.files);

        let file = fileElement.files[0];
        window[object][imgNameProperty] = file.name;

        let fileReader = new FileReader();

        fileReader.onload = function (e) {
            previewId.src = e.target.result;
            window[object][imgProperty] = btoa(e.target.result);

        }
        fileReader.readAsDataURL(file);
    }

}

const clearImg = () => {
    if (employee.emp_photo != null) {
        let userConfirmImgDlt = confirm("Are You Sure To Delete This Image?");
        if (userConfirmImgDlt) {
            employee.emp_photo = null;
            employee.emp_photo_name = null;
            empImg.src = 'resources/images/sigiriya.jpg';
            fileEmpPhoto.files = null;
        } else {
            alert("User Cancelled The Deletion Task")
        }
    }
}



