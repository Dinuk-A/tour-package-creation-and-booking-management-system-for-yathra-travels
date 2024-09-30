window.addEventListener('load', () => {

    loggedUserPrivileges = ajaxGetRequest("/privilege/bymodule/STAY");

    refreshStayTable();

    refreshStayForm();
});

//FOR TABLE
const refreshStayTable = () => {

    stays = ajaxGetRequest("/stay/alldata");

    const displayProperty =
        [
            { dataType: 'text', propertyName: 'name' },
            { dataType: 'function', propertyName: getStayType },
            { dataType: 'function', propertyName: stayLocation },
            { dataType: 'function', propertyName: getStayContacts },
            { dataType: 'text', propertyName: 'maxguestscount' },
            // { dataType: 'text', propertyName: 'maxguestscount' },
            { dataType: 'function', propertyName: getStayStatus }

        ]

    fillDataIntoTable3(stayMainTable, stays, displayProperty, buttonVisibility = true, disableButtonsCommonFn)

    $('#stayMainTable').dataTable();


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

const refreshStayForm = () => {

    stay = new Object;

    formStay.reset();

    //get stay types
    stayTypes = ajaxGetRequest("/staytype/alldata");
    fillDataIntoSelect(stayTypeSelect, 'Please Select The Type', stayTypes, 'name');

    //get statuses
    stayStatuses = ajaxGetRequest("/staystatus/alldata");
    fillDataIntoSelect(stayStatusSelect, 'Please Select The Status', stayStatuses, 'name');

    //get province list
    provinces = ajaxGetRequest("province/alldata");
    fillDataIntoSelect(stayProvinceSelect, 'Please Select The Province', provinces, 'name');

    //get district list 
    districts = ajaxGetRequest("district/alldata");
    fillDataIntoSelect(stayDistrictSelect, 'Please Select The Provice First', districts, 'name');
    stayDistrictSelect.disabled = true;

    stayName.style.border = "1px solid #ced4da";
    stayTypeSelect.style.border = "1px solid #ced4da";
    stayAddress.style.border = "1px solid #ced4da";
    stayProvinceSelect.style.border = "1px solid #ced4da";
    stayDistrictSelect.style.border = "1px solid #ced4da";
    stayContactOne.style.border = "1px solid #ced4da";
    stayContactTwo.style.border = "1px solid #ced4da";
    stayEmail.style.border = "1px solid #ced4da";
    stayMaxGuestCount.style.border = "1px solid #ced4da";
    stayStatusSelect.style.border = "1px solid #ced4da";
    stayNote.style.border = "1px solid #ced4da";

    refreshStayTable();

    stayUpdateBtn.disabled = true;
    stayUpdateBtn.style.cursor = "not-allowed";

}

const getStayType = (ob) => {
    return ob.staytype_id.name;
}

const stayLocation = (ob) => {
    return ob.address + "<br/>" + ob.district_id.name + " <br/> " + ob.district_id.province_id.name + " Province";
}

const getStayContacts = (ob) => {
    return ob.contactnum1 + "<br/>" + ob.contactnum2 + "<br/>" + ob.email;
}

const getStayStatus = (ob) => {
    return ob.staystatus_id.name;
}

const addStay = () => {

    //check errors
    const errors = checkFormErrors();

    if (errors == '') {
        const userConfirm = confirm('Are You Sure To Add ? \n' + stay.name)

        if (userConfirm) {

            //call POST service
            let postServiceResponse = ajaxRequest("/stay", "POST", stay);

            if (postServiceResponse == "OK") {
                alert("Succesfully Saved !!!");
                refreshStayTable();
                vPlaceForm.reset();
                refreshStaysForm();
                $("#modalStay").modal("hide");

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

const checkFormErrors = () => {
    let errors = '';

    if (stay.name == null) {
        errors = errors + " Please Enter The Name \n";
    }

    if (stay.district_id == null) {
        errors = errors + " Please Select The District \n";
    }

    if (stay.staytype_id == null) {
        errors = errors + " Please Select The Stay Type \n";
    }

    if (stay.staystatus_id == null) {
        errors = errors + " Please Select The Status \n";
    }

    if (stay.contactnum1 == null) {
        errors = errors + " Please Enter A Contact Number \n";
    }

    if (stay.email == null) {
        errors = errors + " Please Enter The Email \n";
    }

    if (stay.maxguestscount == null) {
        errors = errors + " Please Enter The Guest Count \n";
    }


    return errors;
}

const stayFormReFill = (ob) => {

    stay = JSON.parse(JSON.stringify(ob));
    stayOldObj = JSON.parse(JSON.stringify(ob));

    $('#modalStay').modal('show');

    stayDistrictSelect.disabled = false;

    stayName.value = stay.name
    stayTypeSelect.value = stay.staytype_id
    stayAddress.value = stay.address

    stayContactOne.value = stay.contactnum1
    stayContactTwo.value = stay.contactnum2
    stayEmail.value = stay.email
    stayMaxGuestCount.value = stay.maxguestscount

    stayNote.value = stay.note

    //get stay types
    stayTypes = ajaxGetRequest("/staytype/alldata");
    fillDataIntoSelect(stayTypeSelect, 'Please Select The Type', stayTypes, 'name', stay.staytype_id.name);

    //get statuses
    stayStatuses = ajaxGetRequest("/staystatus/alldata");
    fillDataIntoSelect(stayStatusSelect, 'Please Select The Status', stayStatuses, 'name', stay.staystatus_id.name);

    provinces = ajaxGetRequest("province/alldata");
    fillDataIntoSelect(stayProvinceSelect, 'Please Select The Province', provinces, 'name', stay.district_id.province_id.name)
    stayProvinceSelect.style.border = "1px solid ced4da";

    districts = ajaxGetRequest("district/alldata");
    fillDataIntoSelect(stayDistrictSelect, 'Please Select The District', districts, 'name', stay.district_id.name);

    stayAddBtn.disabled = true;
    stayAddBtn.style.cursor = "not-allowed";

    stayUpdateBtn.disabled = false;
    stayUpdateBtn.style.cursor = "pointer";

    // if (loggedUserPrivileges.privupdate) {
    //     stayUpdateBtn.disabled = false;
    // } else {
    //     stayUpdateBtn.disabled = true;
    // }


}

const updateStay = () => {

    let errors = checkFormErrors();
    if (errors == "") {
        let updates = showUpdatedValues();
        if (updates == "") {
            alert("No changes detected");
        } else {
            let userConfirm = confirm("Are you sure to proceed ? \n \n" + updates);

            if (userConfirm) {
                let putServiceResponce = ajaxRequest("/stay", "PUT", stay);

                if (putServiceResponce == "OK") {
                    alert("Successfully Updted");
                    $('#modalStay').modal('hide');
                    formStay.reset();
                    refreshStayTable();
                    refreshStayForm();

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

    if (stay.name != stayOldObj.name) {
        updates = updates + "Name have changed to " + stay.name + "\n";
    }

    if (stay.address != stayOldObj.address) {
        updates = updates + "Address have changed to " + stay.address + "\n";
    }

    if (stay.contactnum1 != stayOldObj.contactnum1) {
        updates = updates + "Contact Number #1 have changed to " + stay.contactnum1 + "\n";
    }

    if (stay.contactnum2 != stayOldObj.contactnum2) {
        updates = updates + "Contact Number #2 have changed to " + stay.contactnum2 + "\n";
    }

    if (stay.email != stayOldObj.email) {
        updates = updates + "Email have changed to " + stay.email + "\n";
    }

    if (stay.maxguestscount != stayOldObj.maxguestscount) {
        updates = updates + "Max guests count have changed to " + stay.maxguestscount + "\n";
    }

    if (stay.note != stayOldObj.note) {
        updates = updates + "Note have changed to " + stay.note + "\n";
    }

    if (stay.district_id.name != stayOldObj.district_id.name) {
        updates = updates + "District have changed to " + stay.district_id.name + "\n";
    }

    if (stay.staystatus_id.name != stayOldObj.staystatus_id.name) {
        updates = updates + "Stay status have changed to " + stay.staystatus_id.name + "\n";
    }

    if (stay.staytype_id.name != stayOldObj.staytype_id.name) {
        updates = updates + "Stay type have changed to " + stay.staytype_id.name + "\n";
    }

    return updates

}

const deleteStay = (ob, rowIndex) => {
    console.log('delete btn clkd');

    stayMainTable.children[1].children[rowIndex].style.backgroundColor = 'red';

    setTimeout(function () {
        const userConfirm = confirm('Delete ????');

        if (userConfirm) {
            let deleteServerResponse = ajaxRequest("/stay", "DELETE", ob);
            if (deleteServerResponse == "OK") {
                alert('Deleted succesfully')
                refreshStayTable();
            } else {
                alert("Delete Failed \n" + deleteServerResponse);
            }
        }

    }, 500);
}

//get district list based on selected province
const getDistByProvince = () => {

    const currentProvinceID = JSON.parse(stayProvinceSelect.value).id;
    stayProvinceSelect.style.border = '2px solid green';
    stayDistrictSelect.disabled = false;
    const districts = ajaxGetRequest("district/getdistrictbyprovince/" + currentProvinceID);
    fillDataIntoSelect(stayDistrictSelect, " Please Select The District Now", districts, 'name');

}
