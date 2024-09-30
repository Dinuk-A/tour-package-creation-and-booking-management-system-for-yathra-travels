window.addEventListener('load', () => {

    // loggedUserPrivileges = ajaxGetRequest("/privilege/bymodule/STAY");

    refreshExternalPartyTable();

    refreshExternalPartyForm();
});

//FOR TABLE
const refreshExternalPartyTable = () => {

    lhotels = ajaxGetRequest("/externalparties/alldata");

    const displayProperty =
        [
            { dataType: 'text', propertyName: 'name' },
            { dataType: 'text', propertyName: 'roletype' },
            { dataType: 'function', propertyName: getBothContacts },
            { dataType: 'text', propertyName: 'chargeperday' },
            // { dataType: 'function', propertyName: getEmployeementType },
            { dataType: 'function', propertyName: getStatus }
        ]

    fillDataIntoTable3(externalPartiesMainTable, lhotels, displayProperty, buttonVisibility = true,)
    // disableButtonsCommonFn

    $('#externalPartiesMainTable').dataTable();
}


//to fill table
const getBothContacts = (ob) => {
    return ob.contactone + "</br>" + ob.contacttwo;
}

//to fill table
// const getEmployeementType = (ob) => {
//     if (ob.iscompanyown == true) {
//         return "Yathra Employee";
//     } else {
//         return "External Individual";
//     }

// }

//to fill table
const getStatus = (ob) => {
    return ob.extstatus_id.name;
}

const refreshExternalPartyForm = () => {
    externalparty = new Object;

    formExternalParties.reset();

    //get statuses
    EPStatuses = ajaxGetRequest("/epstatus/alldata");
    fillDataIntoSelect(extEmpStatusSelect, 'Please Select The Status', EPStatuses, 'name');

    selectRole.style.border = "1px solid #ced4da";
    inputFullName.style.border = "1px solid #ced4da";
    inputNIC.style.border = "1px solid #ced4da";
    inputContactOne.style.border = "1px solid #ced4da";
    inputContactTwo.style.border = "1px solid #ced4da";
    chargeRateInput.style.border = "1px solid #ced4da";
    extEmpStatusSelect.style.border = "1px solid #ced4da";
    extEmpNote.style.border = "1px solid #ced4da";
    expAgencyName.style.border = "1px solid #ced4da";
    expAgencyContact.style.border = "1px solid #ced4da";

    refreshExternalPartyTable();

    externalPartiesUpdateBtn.disabled = true;
    externalPartiesUpdateBtn.style.cursor = "not-allowed";

}

const addExternalParty = () => {

    //check errors
    const errors = checkEPFormErrors();

    if (errors == '') {
        const userConfirm = confirm('Are You Sure To Add ? \n' + externalparty.name)

        if (userConfirm) {

            //call POST service
            let postServiceResponse = ajaxRequest("/externalparties", "POST", externalparty);

            if (postServiceResponse == "OK") {
                alert("Succesfully Saved !!!");
                refreshExternalPartyTable();
                formExternalParties.reset();
                refreshExternalPartyForm();
                $("#modalExternalParties").modal("hide");

            } else {
                alert("An Error Occured " + postServiceResponse);
            }
        } else {
            alert('Operation Cancelled By User')
        }
    } else {
        alert('Form Has Followimg Errors \n \n' + errors);
    }

}

const checkEPFormErrors = () => {

    let EpErrors = '';

    if (externalparty.name == null) {
        EpErrors = EpErrors + " Please Enter Full Name \n";
    }

    if (externalparty.roletype == null) {
        EpErrors = EpErrors + " Please Select The Role \n";
    }

    if (externalparty.chargeperday == null) {
        EpErrors = EpErrors + " Please Enter The Rate \n";
    }

    if (externalparty.contactone == null) {
        EpErrors = EpErrors + " Please Enter The Mobile Number \n";
    }

    if (externalparty.extstatus_id == null) {
        EpErrors = EpErrors + " Please Select The Status \n";
    }

    return EpErrors;
}

const refillExternalPartiesForm = (ob) => {

    externalparty = JSON.parse(JSON.stringify(ob));
    externalpartyOldObj = JSON.parse(JSON.stringify(ob));

    $('#modalExternalParties').modal('show');

    EPStatuses = ajaxGetRequest("/epstatus/alldata");
    fillDataIntoSelect(extEmpStatusSelect, 'Please Select The Status', EPStatuses, 'name', externalparty.extstatus_id.name);

    selectRole.value = externalparty.roletype;
    inputFullName.value = externalparty.name;
    inputNIC.value = externalparty.nic;
    inputContactOne.value = externalparty.contactone;
    inputContactTwo.value = externalparty.contacttwo;
    chargeRateInput.value = externalparty.chargeperday;
    extEmpNote.value = externalparty.note;
    expAgencyName.value = externalparty.agencyname;
    expAgencyContact.value = externalparty.extagencycontact;

    externalPartiesAddBtn.disabled = true;
    externalPartiesAddBtn.style.cursor = "not-allowed";

    externalPartiesUpdateBtn.disabled = false;
    externalPartiesUpdateBtn.style.cursor = "pointer";

}

const updateEPRecord = () => {
    let errors = checkEPFormErrors();
    if (errors == "") {
        let updates = showUpdatedValues();
        if (updates == "") {
            alert("No changes detected");
        } else {
            let userConfirm = confirm("Are you sure to proceed ? \n \n" + updates);

            if (userConfirm) {
                let putServiceResponce = ajaxRequest("/externalparties", "PUT", externalparty);

                if (putServiceResponce == "OK") {
                    alert("Successfully Updted");
                    $("#modalexternalparty").modal("hide");
                    formExternalParties.reset();
                    refreshExternalPartyTable();
                    refreshExternalPartyForm();

                } else {
                    alert("An Error Occured " + putServiceResponce);
                }
            } else {
                alert("Operation cancelled by the Operator");
            }
        }
    } else {
        alert("There is an unfilled field \n" + errors);
    }
}

const showUpdatedValues = () => {
    let updates = "";

    if (externalparty.name != externalpartyOldObj.name) {
        updates = updates + " Name has changed \n";
    }

    if (externalparty.nic != externalpartyOldObj.nic) {
        updates = updates + " NIC has changed \n";
    }

    if (externalparty.roletype != externalpartyOldObj.roletype) {
        updates = updates + " Role has changed \n";
    }

    if (externalparty.contactone != externalpartyOldObj.contactone) {
        updates = updates + " Mobile Number has changed \n";
    }

    if (externalparty.contacttwo != externalpartyOldObj.contacttwo) {
        updates = updates + " Land Phone Number has changed \n";
    }

    if (externalparty.chargeperday != externalpartyOldObj.chargeperday) {
        updates = updates + " Charge Rate has changed \n";
    }

    if (externalparty.agencyname != externalpartyOldObj.agencyname) {
        updates = updates + " Agency Name has changed \n";
    }

    if (externalparty.extagencycontact != externalpartyOldObj.extagencycontact) {
        updates = updates + " Agency Contact Number has changed \n";
    }

    if (externalparty.note != externalpartyOldObj.note) {
        updates = updates + " Agency Contact Number has changed \n";
    }

    if (externalparty.extstatus_id.name != externalpartyOldObj.extstatus_id.name) {
        updates = updates + " Status has changed \n";
    }


    return updates
}

const deleteExternalParties = (ob, rowIndex) => {

    // externalPartiesMainTable.children[1].children[rowIndex].style.backgroundColor = 'red';

    let table = $('#externalPartiesMainTable').DataTable();
    let row = table.row(rowIndex).node();  // Gets the DOM element of the row

    $(row).css('background-color', 'red');  // Apply the background color using jQuery

    console.log(rowIndex);

    console.log(rowIndex);

    setTimeout(function () {
        const userConfirm = confirm('Are You Sure To Delete ?');

        if (userConfirm) {
            let deleteServerResponse = ajaxRequest("/externalparties", "DELETE", ob);
            if (deleteServerResponse == "OK") {
                alert('Succesfully Deleted ')
                refreshExternalPartyForm();
            } else {
                alert("Delete Failed \n" + deleteServerResponse);
            }
        } alert('Operator Cancelled The Task');
        window.location.reload();

    }, 300);
}

