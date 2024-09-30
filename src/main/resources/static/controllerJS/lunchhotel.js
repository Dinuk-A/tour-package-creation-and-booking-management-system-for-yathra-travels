window.addEventListener('load', () => {

    // loggedUserPrivileges = ajaxGetRequest("/privilege/bymodule/STAY");

    refreshLunchHotelTable();

    refreshLunchHotelForm();
});

//FOR TABLE
const refreshLunchHotelTable = () => {

    lhotels = ajaxGetRequest("/lunchhotel/alldata");

    const displayProperty =
        [
            { dataType: 'text', propertyName: 'name' },
            { dataType: 'text', propertyName: 'address' },
            { dataType: 'text', propertyName: 'contactnum' },
            { dataType: 'text', propertyName: 'costperhead' },
            { dataType: 'function', propertyName: getLHotelStatus }
        ]

    fillDataIntoTable3(lunchHotelMainTable, lhotels, displayProperty, buttonVisibility = true,)
    // disableButtonsCommonFn

    $('#lunchHotelMainTable').dataTable();
}

const getLHotelStatus = (ob) => {
    return ob.lhotelstts_id.name;
}

const refreshLunchHotelForm = () => {

    lunchhotel = new Object;

    lunchHotelForm.reset();

    //get district list 
    districts = ajaxGetRequest("district/alldata");
    fillDataIntoSelect(selectLHDistrict, 'Please Select The District', districts, 'name');

    //get statuses
    lHotelStatuses = ajaxGetRequest("/lhstts/alldata");
    fillDataIntoSelect(lHStatusSelect, 'Please Select The Status', lHotelStatuses, 'name');

    inputLHPlaceName.style.border = "1px solid #ced4da";
    inputCostperHead.style.border = "1px solid #ced4da";
    selectLHDistrict.style.border = "1px solid #ced4da";
    inputContactNum.style.border = "1px solid #ced4da";
    inputLHNote.style.border = "1px solid #ced4da";
    inputLHAddress.style.border = "1px solid #ced4da";
    lHStatusSelect.style.border = "1px solid #ced4da";

    // stayName.style.border = "1px solid #ced4da";
    // stayName.style.border = "1px solid #ced4da";
    // stayName.style.border = "1px solid #ced4da";

    refreshLunchHotelTable();
    lunchHotelUpdateBtn.disabled = true;
    lunchHotelUpdateBtn.style.cursor = "not-allowed";

}

const addLunchHotel = () => {

    //check errors
    const errors = checkLHFormErrors();

    if (errors == '') {
        const userConfirm = confirm('Are You Sure To Add ? \n' + lunchhotel.name)

        if (userConfirm) {

            //call POST service
            let postServiceResponse = ajaxRequest("/lunchhotel", "POST", lunchhotel);

            if (postServiceResponse == "OK") {
                alert("Succesfully Saved !!!");
                refreshLunchHotelTable();
                lunchHotelForm.reset();
                
                refreshLunchHotelForm();
                $("#modalLunchHotel").modal("hide");

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

const checkLHFormErrors = () => {

    let lhErrors = '';

    if (lunchhotel.name == null) {
        lhErrors = lhErrors + " Please Enter Hotel Name \n";
    }

    if (lunchhotel.costperhead == null) {
        lhErrors = lhErrors + " Please Enter The Cost \n";
    }

    if (lunchhotel.district_id == null) {
        lhErrors = lhErrors + " Please Select The District \n";
    }

    if (lunchhotel.address == null) {
        lhErrors = lhErrors + " Please Enter The Address \n";
    }

    if (lunchhotel.contactnum == null) {
        lhErrors = lhErrors + " Please Enter The Contact Number \n";
    }

    if (lunchhotel.lhotelstts_id == null) {
        lhErrors = lhErrors + " Please Select The Status \n";
    }

    // if (stay.district_id == null) {
    //     errors = errors + " Please Select The District \n";
    // }

    return lhErrors;
}

const refillLunchHotel = (ob) => {

    lunchhotel = JSON.parse(JSON.stringify(ob));
    lunchhotelOldObj = JSON.parse(JSON.stringify(ob));

    $('#modalLunchHotel').modal('show');

    inputLHPlaceName.value = lunchhotel.name;
    inputCostperHead.value = lunchhotel.costperhead;

    districts = ajaxGetRequest("district/alldata");
    fillDataIntoSelect(selectLHDistrict, 'Please Select The District', districts, 'name', lunchhotel.district_id.name);

    inputContactNum.value = lunchhotel.address;
    inputLHNote.value = lunchhotel.note;
    inputLHAddress.value = lunchhotel.contactnum;

    lHotelStatuses = ajaxGetRequest("/lhstts/alldata");
    fillDataIntoSelect(lHStatusSelect, 'Please Select The Status', lHotelStatuses, 'name', lunchhotel.lhotelstts_id.name);

    lunchHotelAddBtn.disabled = true;
    lunchHotelAddBtn.style.cursor = "not-allowed";

    lunchHotelUpdateBtn.disabled = false;
    lunchHotelUpdateBtn.style.cursor = "pointer";

}

const updateLHRecord = () => {
    let errors = checkLHFormErrors();
    if (errors == "") {
        let updates = showUpdatedValues();
        if (updates == "") {
            alert("No changes detected");
        } else {
            let userConfirm = confirm("Are you sure to proceed ? \n \n" + updates);

            if (userConfirm) {
                let putServiceResponce = ajaxRequest("/lunchhotel", "PUT", lunchhotel);

                if (putServiceResponce == "OK") {
                    alert("Successfully Updted");
                    $("#modalLunchHotel").modal("hide");
                    lunchHotelForm.reset();
                    refreshLunchHotelTable();
                    refreshLunchHotelForm();


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

    if (lunchhotel.name != lunchhotelOldObj.name) {
        updates = updates + " Hotel Name has changed \n";
    }

    if (lunchhotel.costperhead != lunchhotelOldObj.costperhead) {
        updates = updates + " Meal cost has changed \n";
    }

    if (lunchhotel.note != lunchhotelOldObj.note) {
        updates = updates + " Note has changed \n";
    }

    if (lunchhotel.address != lunchhotelOldObj.address) {
        updates = updates + " Hotel address has changed \n";
    }

    if (lunchhotel.contactnum != lunchhotelOldObj.contactnum) {
        updates = updates + " Hotel contact number has changed \n";
    }

    if (lunchhotel.lhotelstts_id.name != lunchhotelOldObj.lhotelstts_id.name) {
        updates = updates + " Hotel Status has changed \n";
    }

    if (lunchhotel.district_id.name != lunchhotelOldObj.district_id.name) {
        updates = updates + " Hotel district has changed \n";
    }
    return updates
}

const deleteLunchHotel = (ob, rowIndex) => {
  
    lunchHotelMainTable.children[1].children[rowIndex].style.backgroundColor = 'red';

    setTimeout(function () {
        const userConfirm = confirm('Delete ????');

        if (userConfirm) {
            let deleteServerResponse = ajaxRequest("/lunchhotel", "DELETE", ob);
            if (deleteServerResponse == "OK") {
                alert('Deleted succesfully')
                refreshLunchHotelForm();
            } else {
                alert("Delete Failed \n" + deleteServerResponse);
            }
        }

    }, 500);
}

// const getDistByProvince = () => {

//     const currentProvinceID = JSON.parse(stayProvinceSelect.value).id;
//     stayProvinceSelect.style.border = '2px solid green';
//     stayDistrictSelect.disabled = false;
//     const districts = ajaxGetRequest("district/getdistrictbyprovince/" + currentProvinceID);
//     fillDataIntoSelect(stayDistrictSelect, " Please Select The District Now", districts, 'name');

// }

// const disableButtonsCommonFn = (rowOb) => {

//     if (!loggedUserPrivileges.privupdate) {
//         btnEdit.disabled = true;
//         btnEdit.style.cursor = "not-allowed"
//     }

//     if (!loggedUserPrivileges.privdelete) {
//         btnDelete.disabled = true;
//         btnDelete.style.cursor = "not-allowed"
//     } else {
//         if (rowOb.employeestatus_id.name == "Deleted") {
//             btnDelete.disabled = true;
//             btnDelete.style.cursor = "not-allowed";
//         }
//     }
//     if (!loggedUserPrivileges.privselect) {
//         btnPrint.disabled = true;
//         btnPrint.style.cursor = "not-allowed"
//     }
// }