window.addEventListener('load', () => {

    loggedUserPrivileges = ajaxGetRequest("/privilege/bymodule/BOOKING");

    //call fn for refresh/show data in table
    refreshBookingTable();

    //for booking form
    refreshBookingForm();

    refreshNewRentalvehiForm();
    refreshNewExternalEmployeeForm();

})

//main table
const refreshBookingTable = () => {

    bookings = ajaxGetRequest("/booking/alldata");

    const displayProperty = [
        { dataType: 'text', propertyName: 'bookingcode' },
        { dataType: 'function', propertyName: getBookedPackage },
        { dataType: 'function', propertyName: getClientNameAndContacts },
        { dataType: 'function', propertyName: getBookingStatus }
    ]

    fillDataIntoTable3(bookingMainTable, bookings, displayProperty, buttonVisibility = true, loggedUserPrivileges)

    $('#bookingMainTable').dataTable();
}

//to support main table
const getClientNameAndContacts = (ob) => {
    return ob.client_id.clientname + "</br>" + ob.client_id.contactnum + "</br>" + ob.client_id.email;
}

//to support main table
const getBookingStatus = (ob) => {
    return ob.bookingstatus_id.name;
}

const getBookedPackage = (ob) => {
    return ob.tourpackage_id.packagecode + "</br>" +ob.tourpackage_id.packagename ;
}

//refresh main form
const refreshBookingForm = () => {
    booking = new Object;

    //get Status list
    bookingStatuses = ajaxGetRequest("/bookingstatus/alldata");
    fillDataIntoSelect(bookingStatusSelect, 'Please Select The Status', bookingStatuses, 'name')

    //get 3 vehicle type lists
    vehicleType = ajaxGetRequest("/vtypes/alldata");

    //in main form
    fillDataIntoSelect(bookingVehitype, 'Select Type', vehicleType, 'name');
    fillDataIntoSelect(rentalVehitype, 'Select Type', vehicleType, 'name');

    //to the field that in the secondary model
    fillDataIntoSelect(selectVehiType, 'Select Type', vehicleType, 'name');

    const externalDrivers = ajaxGetRequest("exp/drivers")
    fillDataIntoSelect(externalDriverSelect, "Please Select Driver", externalDrivers, 'name');

    //get available vehicles
    // availableVehicleSelect
    // bookingAddBtn

    bookingCodeInput.style.border = "1px solid #ced4da";
    bookedPackageSelect.style.border = "1px solid #ced4da";
    inquiryOfBookingSelect.style.border = "1px solid #ced4da";
    bookingClientNameInput.style.border = "1px solid #ced4da";
    bookingEmail.style.border = "1px solid #ced4da";
    bookingClientContact.style.border = "1px solid #ced4da";
    bookingNote.style.border = "1px solid #ced4da";
    bookingStatusSelect.style.border = "1px solid #ced4da";
}

//for update button
const updateBooking = () => {
    let errors = checkBookingFormErrors();
    if (errors == '') {
        let updates = getBookingFormUpdates();
        if (updates == '') {
            alert('No Changes Detected');
        } else {
            let userResponse = confirm("Sure To Update ? \n \n " + updates);
            if (userResponse) {
                let putServiceResponce = ajaxRequest("/booking", "PUT", booking);

                if (putServiceResponce == "OK") {
                    alert("Successfully Updated");
                    $('#bookingMainModel').modal('hide');
                    refreshBookingTable();
                    bookingMgtForm.reset();
                    refreshBookingForm();

                } else {
                    alert("An Error Occured " + putServiceResponce);
                }

            } else {
                alert('Operator Cancelled The Task')
            }
        }

    } else {
        alert('Form has following errors \n ' + errors);
    }
}

//to CHECK ERRORS IN booking record
const checkBookingFormErrors = () => {
    let errors = '';

    if (booking.driver == null || booking.external_driver == null) {
        errors = errors + " Please Choose a Driver \n";
    }

    if (booking.guide == null || booking.external_guide == null) {
        errors = errors + " Please Choose a Guide \n";
    }

    if (booking.vehicle_id == null) {
        errors = errors + " Please Choose a Vehicle \n";
    }

    if (booking.bookingstatus_id == null) {
        errors = errors + " Please Select the Status \n";
    }

    return errors;

}

//to UPDATE booking record
const getBookingFormUpdates = () => {
    let updates = '';

    if (booking.note != oldBookingObj.note) {
        updates = updates + " Note has changed \n";
    }

    // if (booking.startdate != oldBookingObj.startdate) {
    //     updates = updates + " Start date has changed \n";
    // }

    // if (booking.enddate != oldBookingObj.enddate) {
    //     updates = updates + " End Date has changed \n";
    // }

    if (booking.bookingstatus_id.name != oldBookingObj.bookingstatus_id.name) {
        updates = updates + " Booking Status has changed \n";
    }

    if ((booking.driver != oldBookingObj.driver) || (booking.external_driver != oldBookingObj.external_driver)) {
        updates = updates + " Driver has changed \n";
    }

    if ((booking.guide != oldBookingObj.guide) || (booking.external_guide != oldBookingObj.external_guide)) {
        updates = updates + "Guide has changed \n";
    }

    if (booking.vehicle_id != oldBookingObj.vehicle_id) {
        updates = updates + "Vehicle has changed \n";
    }
    return updates;
}

//for edit button
const bookingFormReFill = (obj) => {
    booking = JSON.parse(JSON.stringify(obj));
    oldBookingObj = JSON.parse(JSON.stringify(obj));

    bookingCodeInput.value = booking.bookingcode;

    bookingStatuses = ajaxGetRequest("/bookingstatus/alldata");
    fillDataIntoSelect(bookingStatusSelect, 'Please Select The Status', bookingStatuses, 'name', booking.bookingstatus_id.name);

    tourPackages = ajaxGetRequest("/tourpackage/alldata");
    fillDataIntoSelect(bookedPackageSelect, 'Please Select The Package', tourPackages, 'packagecode', booking.tourpackage_id.packagecode);

    basedInquiry = ajaxGetRequest("/inquiry/alldata");
    fillDataIntoSelect(inquiryOfBookingSelect, 'Please Select The Inquiry', basedInquiry, 'inqcode', booking.inquiry_id.inqcode);

    bookingClientNameInput.value = booking.client_id.clientname;
    bookingEmail.value = booking.client_id.email;
    bookingClientContact.value = booking.client_id.contactnum;

    bookingStartDate.value = booking.startdate;
    bookingEndDate.value = booking.enddate;
    bookingNote.value = booking.note;

    bookingUpdateBtn.disabled = false;
    bookingUpdateBtn.style.cursor = "pointer";

    $('#bookingMainModel').modal('show');
}

//get available company own vehicles by given date
const getAvailableCompanyDrivers = () => {
    var startDate = bookingStartDate.value;
    var lastDate = bookingEndDate.value;
    const availableDriversList = ajaxGetRequest("emp/availabledriver/" + startDate + "/" + lastDate)
    fillDataIntoSelect(availableDriverSelect, 'Available Drivers', availableDriversList, 'fullname');
}

const getAvailableGuideList = () => {
    var startDate = bookingStartDate.value;
    var lastDate = bookingEndDate.value;
    const availableGuideList = ajaxGetRequest("emp/availableguide/" + startDate + "/" + lastDate)
    fillDataIntoSelect(availableGuideSelect, 'Available Guides', availableGuideList, 'fullname');
}

const getAvailableCompVehicles = () => {

    var startDate = bookingStartDate.value;
    var lastDate = bookingEndDate.value;

    let selectedOption = JSON.parse(bookingVehitype.value);
    let vehitype = selectedOption.id;

    console.log('vehitype', vehitype);

    const availableVehiList = ajaxGetRequest("vehi/availablevehiclesbyvehitype/" + startDate + "/" + lastDate + "/" + vehitype)
    fillDataIntoSelect(availableVehicleSelect, 'Available Vehicles', availableVehiList, 'licencenumber');

}

const getPrevRentedVehicles = () => {

    let selectedOption = JSON.parse(rentalVehitype.value);
    let vehitype = selectedOption.id;

    console.log('vehitype', vehitype);

    const availableVehiList = ajaxGetRequest("vehi/previousrentalvehicles/" + vehitype);

    fillDataIntoSelect(rentalVehiSelect, 'Available Vehicles', availableVehiList, 'licencenumber');
}

//apply changes depend on vehicle source type
const changesBasedOnVehicleSource = () => {

    if (companyOwnVehiRadio.checked) {
        companyVehicleSection.classList.remove('d-none');
        rentedVehicleSection.classList.add('d-none');

        //remove and unbind rented vehi values
        booking.vehicle_id = null;
        rentalVehiSelect.value = '';

        //refresh rented vehi fields
        rentalVehiSelect.style.border = "1px solid #ced4da";

        //if company, select suitable vehi type and select available vehicles
        let startDate = bookingStartDate.value;
        let lastDate = bookingEndDate.value;
        // let vehiType = JSON.parse(bookingVehitype.value).id;
        const availableVehiList = ajaxGetRequest(" vehi/availablevehicles/" + startDate + "/" + lastDate)
        fillDataIntoSelect(availableVehicleSelect, "Please Select Vehicle", availableVehiList, 'licencenumber');

    }
    else if (rentalVehiRadio.checked) {
        companyVehicleSection.classList.add('d-none');
        rentedVehicleSection.classList.remove('d-none');

        //remove and unbind company vehi values
        booking.vehicle_id = null;
        availableVehicleSelect.value = '';

        //refresh rented company fields
        availableVehicleSelect.style.border = "1px solid #ced4da";

        //if outsource, select suitable vehi type from vehicles table and vehicles from previous list
        //or add a new vehicle 
        const rentedVehicleList = ajaxGetRequest("vehi/rentedvehicles")
        fillDataIntoSelect(rentalVehiSelect, "Please Select Vehicle", rentedVehicleList, 'licencenumber');

    }
}

//apply changes depend on Driver source type
const changesBasedOnDriverSource = () => {

    if (companyOwnDriver.checked) {
        companyDriverSection.classList.remove('d-none');
        externalDriverSection.classList.add('d-none')

        booking.external_driver = null;
        externalDriverSelect.value = '';
        externalDriverSelect.style.border = "1px solid #ced4da";

        let startDate = bookingStartDate.value;
        let lastDate = bookingEndDate.value;

        const availableDriversList = ajaxGetRequest(" emp/availabledriver/" + startDate + "/" + lastDate)
        fillDataIntoSelect(availableDriverSelect, "Please Select Driver", availableDriversList, 'fullname');

    }
    if (rentalDriver.checked) {
        companyDriverSection.classList.add('d-none');
        externalDriverSection.classList.remove('d-none')

        booking.driver = null;
        availableDriverSelect.value = '';
        availableDriverSelect.style.border = "1px solid #ced4da";

        const externalDrivers = ajaxGetRequest("exp/drivers")
        fillDataIntoSelect(externalDriverSelect, "Please Select Driver", externalDrivers, 'name');

    }
}

//apply changes depend on guide source type
const changesBasedOnGuideSource = () => {

    if (companyOwnGuide.checked) {
        companyGuideSection.classList.remove('d-none');
        externalGuideSection.classList.add('d-none')

        booking.external_guide = null;
        externalGuideSelect.value = '';
        externalGuideSelect.style.border = "1px solid #ced4da";

        let startDate = bookingStartDate.value;
        let lastDate = bookingEndDate.value;

        const availableGuidesList = ajaxGetRequest(" emp/availableguide/" + startDate + "/" + lastDate)
        fillDataIntoSelect(availableGuideSelect, "Please Select Guide", availableGuidesList, 'fullname');

    }
    if (rentalGuides.checked) {
        companyGuideSection.classList.add('d-none');
        externalGuideSection.classList.remove('d-none')

        booking.guide = null;
        availableGuideSelect.value = '';
        availableGuideSelect.style.border = "1px solid #ced4da";

        const externalGuides = ajaxGetRequest("exp/guides")
        fillDataIntoSelect(externalGuideSelect, "Please Select Guide", externalGuides, 'name');

    }
}

//rentalVehicleObj object ekak hadanna
const refreshNewRentalvehiForm = () => {

    rentalVehicleObj = new Object;

    vehicleType = ajaxGetRequest("/vtypes/alldata");
    fillDataIntoSelect(selectVehiType, 'Select Type', vehicleType, 'name')

    vStatus = ajaxGetRequest("/vstatus/alldata");
    fillDataIntoSelect(selectVehiStatus, 'Please Select The Status', vStatus, 'name', 'Available For Service')
    rentalVehicleObj.vehistatus_id = JSON.parse(selectVehiStatus.value);
    selectVehiStatus.style.border = "1px solid lime";

    selectVehiType.style.border = "1px solid #ced4da";
    newRentedVehiNumberPlate.style.border = "1px solid #ced4da";
    // intSeatCount.style.border = "1px solid #ced4da";
    vehiAgencyName.style.border = "1px solid #ced4da";
    vehiAgencyContact.style.border = "1px solid #ced4da";
    vehiInputNote.style.border = "1px solid #ced4da";
    // selectVehiStatus.style.border = "1px solid #ced4da";

}

const checkNewRentalVehiFormErrors = () => {
    let errors = '';

    if (rentalVehicleObj.vehitype_id == null) {
        errors = errors + " Please Choose The Vehicle Type \n";
    }

    if (rentalVehicleObj.licencenumber == null) {
        errors = errors + " Please Enter The Vehicle Number Plate \n";
    }

    if (rentalVehicleObj.agencyname == null) {
        errors = errors + " Please Enter Agency's Name \n";
    }

    if (rentalVehicleObj.agencycontactnum == null) {
        errors = errors + " Please Enter Agency's Contact Number  \n";
    }

    return errors;
}

//to add a new external vehicle
const submitNewRentalVehi = () => {

    let errors = checkNewRentalVehiFormErrors();

    if (errors == '') {
        let userConfirm = confirm('Are You Sure To Add ?');
        if (userConfirm) {
            rentalVehicleObj.iscompanyown = false;
            let postServiceResponse = ajaxRequest("/vehicle", "POST", rentalVehicleObj);
            if (postServiceResponse == "OK") {
                alert('New Rented Vehicle Added');

                const rentedVehicleList = ajaxGetRequest("vehi/previousrentalvehicles/" + vehitype);
                fillDataIntoSelect(rentalVehiSelect, "Please Select Vehicle", rentedVehicleList, 'licencenumber', newRentedVehiNumberPlate.value);

                booking.vehicle_id = JSON.parse(rentalVehiSelect.value)

                refreshNewRentalvehiForm();
                newRentalVehiMgtform.reset();

                $('#newRentelVehiModal').modal('hide');
                $('#bookingMainModel').modal('show');


            } else {
                alert('Post Service Failed \n' + postServiceResponse);
            }
        } else {
            alert("Operator Cancelled The Task")
        }
    } else {
        alert('Form Has Following Errors \n \n' + errors)
    }
}

//newExtPartyObj object ekak hadanna
const refreshNewExternalEmployeeForm = () => {

    newExtPartyObj = new Object;

    selectExternalEmpRole.style.border = "1px solid #ced4da";
    inputFullName.style.border = "1px solid #ced4da";
    inputNIC.style.border = "1px solid #ced4da";
    inputContactOne.style.border = "1px solid #ced4da";
    inputAgencyName.style.border = "1px solid #ced4da";
    inputAgencyContact.style.border = "1px solid #ced4da";
    chargeRateInput.style.border = "1px solid #ced4da";
    // extEmpStatusSelect.style.border = "1px solid #ced4da";
    extEmpNote.style.border = "1px solid #ced4da";

    EPStatuses = ajaxGetRequest("/epstatus/alldata");
    fillDataIntoSelect(extEmpStatusSelect, 'Please Select The Status', EPStatuses, 'name', 'Active');
    newExtPartyObj.extstatus_id = JSON.parse(extEmpStatusSelect.value);
    extEmpStatusSelect.style.border = "1px solid lime";

}

const checkNewExtEmpErrors = () => {
    let errors = '';

    if (newExtPartyObj.roletype == null) {
        errors = errors + " Please Select External Employee Role \n";
    }

    if (newExtPartyObj.name == null) {
        errors = errors + " Please Enter External Employee Name \n";
    }

    if (newExtPartyObj.nic == null) {
        errors = errors + " Please Enter External Employee NIC number \n";
    }

    if (newExtPartyObj.contactone == null) {
        errors = errors + " Please Enter External Employee Contact Number \n";
    }

    if (newExtPartyObj.agencyname == null) {
        errors = errors + " Please Enter Agency Name \n";
    }

    if (newExtPartyObj.extagencycontact == null) {
        errors = errors + " Please Enter Agency's Contact Number  \n";
    }

    if (newExtPartyObj.chargeperday == null) {
        errors = errors + " Please Enter External Employees Charge   \n";
    }

    if (newExtPartyObj.extstatus_id == null) {
        errors = errors + " Please Select External Employees Status  \n";
    }
    return errors;
}

//to add new external employees
const submitNewExtEmployee = () => {
    let errors = checkNewExtEmpErrors();
    if (errors == '') {
        let userConfirm = confirm('Are You Sure To Add ?');
        if (userConfirm) {

            let postServiceResponse = ajaxRequest("/externalparties", "POST", newExtPartyObj);
            if (postServiceResponse == "OK") {
                alert('New External Employee Added');

                // const rentedVehicleList = ajaxGetRequest("externalparties/rentedvehicles")
                // fillDataIntoSelect(rentalVehiSelect, "Please Select Vehicle", rentedVehicleList, 'licencenumber', newRentedVehiNumberPlate.value);

                // booking.vehicle_id = JSON.parse(rentalVehiSelect.value);

                refreshNewExternalEmployeeForm();
                newExtEmpMgtForm.reset();


                $('#newExternalDriverOrGuideModal').modal('hide');
                $('#bookingMainModel').modal('show');


            } else {
                alert('Post Service Failed \n' + postServiceResponse);
            }
        } else {
            alert("Operator Cancelled The Task")
        }
    } else {
        alert('Form Has Following Errors \n \n' + errors)
    }
}


//for add button 💥 dont need an add button 
const addBooking = () => {
    //check form error
    const errors = checkBookingFormErrors();

    if (errors == '') {
        const userConfirm = confirm('Are You Sure To Add ? \n' + attraction.name)

        if (userConfirm) {

            //call POST service
            let postServiceResponse = ajaxRequest("/booking", "POST", booking);

            if (postServiceResponse == "OK") {
                alert("Succesfully Saved !!!");
                bookingMgtForm.reset();
                refreshBookingForm();
                refreshBookingTable();
                $('#bookingMainModel').modal('hide');

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

//for delete btn  💥💥 dont need
const deleteBooking = (ob, row) => {

    bookingMainTable.children[1].children[row].style.backgroundColor = 'red';

    console.log(row);

    setTimeout(function () {
        const userConfirm = confirm('Are You Sure To Delete ?');

        if (userConfirm) {
            let deleteServerResponse = ajaxRequest("/booking", "DELETE", ob);

            if (deleteServerResponse == "OK") {
                alert("successfully Deleted");
                refreshBookingTable();
            } else {
                alert("An Error Occured \n" + deleteServerResponse);
            }
        } else {
            alert('Operator Cancelled The Task');
            window.location.reload();
        }
    }, 300)

}


//**/*get popular tourpackage*/
/*conifirm una ewa saha advanced pay krpu tiken thama balanne*/
/*meken denne list ekk*/
// select * from booking where bookingstatus_id = 5 or bookingstatus_id = 6 and tourpackage_id ?=1
/*Backend Code Springboot*/
/*
public List<Booking> getPopularBooking(@PathVariable Integer tourPackageID){
  return bookingDAO.getPopularBooking(tourPackageID);
}
*/

/*Frontend Code JS*/

/*
const tourpackges = ajaxGetRequest("/tourpackages/getall");
let data = []
tourpackges.forEach(tour=>{
  data.push({name:tour.name , count: ajaxGetRequest("/booking/getPopularBooking/"+tour.id).length})
})

meke data eke thiynewa tourpakcgae eke namei eke order krpu count ekai
*/ 

/**select * from booking where startdate =?1

@GetMapping(value="/Booking/getBookingByStartDate/{date}")
// public List<Booking> getBookingByStartDate(@PathVariable String date){
 return bookingDAO.getBookingByStartDate(date)
}


window.addEventListner('load',()=>{

 const today = new Date().toISOString().split('T')[0]
 const bookingToday = ajaxGetRequest("/Booking/getBookingByStartDate/"+today);

}) */