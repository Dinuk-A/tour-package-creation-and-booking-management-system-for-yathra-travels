window.addEventListener('load', () => {
    refreshInquiryForm();
    refreshInquiryTable();


    const todayRecievedDate = new Date().toISOString().split('T')[0];
    document.getElementById('inqRecievedDate').setAttribute('max', todayRecievedDate);


    const dateInput = document.getElementById('inqArrivalDate');   // Get the date input element    
    const today = new Date();  // Get today's date   
    const minDate = new Date(today.setDate(today.getDate() + 5));   // Add 7 days to today's date   
    const formattedDate = minDate.toISOString().split('T')[0];   // Format the date as yyyy-mm-dd    
    dateInput.setAttribute('min', formattedDate);  // Set the min attribute of the date input
});

const refreshInquiryTable = () => {

    // all inquiries sort by the status
    inqList = ajaxGetRequest("/inquiry/inqsexceptnewandinprogress");

    const displayProperty = [

        { dataType: 'text', propertyName: 'inqcode' },
        { dataType: 'text', propertyName: 'recievedmethod' },
        { dataType: 'text', propertyName: 'inqtype' },
        { dataType: 'text', propertyName: 'clientname' },
        { dataType: 'function', propertyName: getClientNationality },
        { dataType: 'function', propertyName: getTimeStamp },
        { dataType: 'function', propertyName: getInquiryStatus }

    ]

    fillDataIntoTable3(inqMainTable, inqList, displayProperty, buttonVisibility = true) //disableButtonsCommonFn , loggedUserPrivileges 

    $('#inqMainTable').dataTable();
}

//for table
const getTimeStamp = (ob) => {
    return ob.recieveddate + "</br>" + ob.recievedtime
}

//for table
const getClientNationality = (ob) => {
    return ob.nationality.countryname;
}

//for table
const getInquiryStatus = (ob) => {
    return ob.inquirystatus.name;
}

const ifInqTypeIsGeneral = () => {
    if (inquiry.inqtype == "General") {
        inqInterestedPkg.disabled = true;
        inquiry.based_tpkg_id = null;
    } else {
        inqInterestedPkg.disabled = false;
        inqInterestedPkg.style.border = "1px solid #ced4da";
    }
}

//for natonality field
const changeLable = () => {
    if (InqClientNationality.value == "Sri Lanka") {
        lblForNICorPpt.innerText = "NIC: "
    } else {
        lblForNICorPpt.innerText = "Passport: "
    }
}

//for 2nd contact num field
const sameContactError = () => {
    if (inqAdditionalContact.value == inqContactOne.value) {
        alert("Enter A Different Number From Previous Contact Number")
        inqAdditionalContact.style.border = '2px solid red';
        inquiry.contactnumtwo = null
    } else {
        inputFieldValidator(this, '', 'inquiry', 'contactnumtwo');
    }
}

const refreshInquiryForm = () => {

    inquiry = new Object();

    formManualInquiry.reset();

    disableFutureDates(inqRecievedDate);

    //fillDataIntoDataList💥💥💥 not working
    nationalityList = ajaxGetRequest('/nationality/alldata')
    fillDataIntoDataList(dataListNationality, nationalityList, 'countryname');

    pkgList = ajaxGetRequest('/tourpackageforweb/alldata')
    fillDataIntoSelect(inqInterestedPkg, 'Select Package', pkgList, 'packagename');

    pkgList = ajaxGetRequest('/tourpackagecustom/alldata')
    fillDataIntoSelect(customizedPackage, 'Select Customized Package', pkgList, 'packagename');

    inqStatusList = ajaxGetRequest('/inqstatus/alldata')
    fillDataIntoSelect(inqStatus, 'Select Status', inqStatusList, 'name','New');
    inquiry.inquiry_status_id = JSON.parse(inqStatus.value);
    inqStatus.style.border = "2px solid lime"

    //initially UPDATE button should be disabled (in ADD mode)
    inqManualUpdateBtn.disabled = true;
    inqManualUpdateBtn.style.cursor = "not-allowed";

    inqCodeInput.style.border = "1px solid #ced4da";
    inqType.style.border = "1px solid #ced4da";
    inqInterestedPkg.style.border = "1px solid #ced4da";
    inqRecievedMethod.style.border = "1px solid #ced4da";
    inqRecievedDate.style.border = "1px solid #ced4da";
    // inqRecievedTime.style.border = "1px solid #ced4da";
    inqMainEnquiry.style.border = "1px solid #ced4da";
    // inqAdultCount.style.border = "1px solid #ced4da";
    // inqChildCount.style.border = "1px solid #ced4da";
    // inqPrefStayType.style.border = "1px solid #ced4da";
    inqPickupLoc.style.border = "1px solid #ced4da";
    onqDropOffLock.style.border = "1px solid #ced4da";
    inqNote.style.border = "1px solid #ced4da";
    inqArrivalDate.style.border = "1px solid #ced4da";
    // inqRecievedAddress.style.border = "1px solid #ced4da";
    inqClientName.style.border = "1px solid #ced4da";
    InqClientNationality.style.border = "1px solid #ced4da";
    inqClientPassportNumorNIC.style.border = "1px solid #ced4da";
    inqContactOne.style.border = "1px solid #ced4da";
    inqAdditionalContact.style.border = "1px solid #ced4da";
    inqPrefferedContactMethod.style.border = "1px solid #ced4da";

}

//for the add button
const addManualInq = () => {
    const errors = checkManualInqErrors();
    if (errors == '') {
        const userConfirm = confirm('Are You Sure To Add ? \n');

        if (userConfirm) {
            let postServiceResponse = ajaxRequest("/inquiry", "POST", inquiry);

            if (postServiceResponse == "OK") {
                alert("Succesfully Saved !!!");
                formManualInquiry.reset();
                refreshInquiryForm();
                refreshInquiryTable();
                $('#modalInquiry').modal('hide');

                window.location.assign("/inquiryresponse");
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

const checkManualInqErrors = () => {
    let errors = "";

    if (inquiry.inqtype == null) {
        errors = errors + " Please Select The Inquiry Type \n";
    }

    if (inquiry.inqtype == "Package-Related" && inquiry.based_tpkg_id == null) {
        errors = errors + " Please Select The interested Tour Package \n";
    }

    if (inquiry.recievedmethod == null) {
        errors = errors + " Please Select The Source Of Inquiry \n";
    }

    // if (inquiry.recievedmethod == "Phone Call" && inquiry.recievedcontactoremail == null) {
    //     errors = errors + " Please Enter The Source Phone Number \n";
    // }

    // if (inquiry.recievedmethod == "Email" && inquiry.recievedcontactoremail == null) {
    //     errors = errors + " Please Enter The Source Email Address \n";
    // }

    if (inquiry.recieveddate == null) {
        errors = errors + " Please Enter The Inquiry Recieved Date  \n";
    }

    // if (inquiry.recievedtime == null) {
    //     errors = errors + " Please Enter The Inquiry Recieved Time  \n";
    // }

    if (inquiry.enquiry == null) {
        errors = errors + " Please Enter The Main Enquiry Text  \n";
    }

    if (inquiry.clientname == null) {
        errors = errors + " Please Enter The Client's Name  \n";
    }

    if (inquiry.nationality == null) {
        errors = errors + " Please Select The Client's Nationality  \n";
    }

    if (inquiry.contactnum == null) {
        errors = errors + " Please Enter The Client's Contact Number  \n";
    }

    if (inquiry.prefcontactmethod == null) {
        errors = errors + " Please Select The Client's Preferred Contact Method  \n";
    }

    if (inquiry.inquirystatus == null) {
        errors = errors + " Please Enter The Inquiry Status  \n";
    }

    return errors;
}

const refillInquiryForm = (ob) => {

    inquiry = JSON.parse(JSON.stringify(ob));
    oldInquiry = JSON.parse(JSON.stringify(ob));

    $('#modalInquiry').modal('show');

    inqCodeInput.value = inquiry.inqcode;
    inqRecievedMethod.value = inquiry.recievedmethod;
    inqType.value = inquiry.inqtype;
    inqRecievedDate.value = inquiry.recieveddate;
    inqClientTitle.value = inquiry.clienttitle
    InqClientNationality.value = inquiry.nationality.countryname;
    inqClientName.value = inquiry.clientname
    inqClientPassportNumorNIC.value = inquiry.passportnumornic;
    inqContactOne.value = inquiry.contactnum;
    inqAdditionalContact.value = inquiry.contactnumtwo;
    inqPrefferedContactMethod.value = inquiry.prefcontactmethod;
    inqMainEnquiry.value = inquiry.enquiry;
    inqClientEmail.value = inquiry.email;
    inqNote.value = inquiry.note;
    inqPickupLoc.value = inquiry.pickuplocation;
    onqDropOffLock.value = inquiry.dropofflocation;
    inqArrivalDate.value = inquiry.arrivaldate;
    inqLocalAdultCountInput.value = inquiry.inqlocaladultcount;
    inqLocalChildCountInput.value = inquiry.inqlocalchildcount;
    inqForeignAdultCountInput.value = inquiry.inqforeignadultcount;
    inqForeignChildCountInput.value = inquiry.inqforeignchildcount;

    if (inquiry.isguideinclude) {
        guideYes.checked = true;
    } else {
        guideNo.checked = true;
    }

    if (inquiry.based_tpkg_id == null) {
        inqInterestedPkg.disabled = true;
    } else {
        tourpkgs = ajaxGetRequest("/tourpackageforweb/alldata");
        fillDataIntoSelect(inqInterestedPkg, 'Please Select The Interested package', tourpkgs, 'packagename', inquiry.based_tpkg_id.packagename)
    }

    inqInterestedPkg.disabled = false;

    //me datalist eka refill karanne kohomada?💥💥💥
    // nationalityListEdit = ajaxGetRequest('/nationality/alldata')
    // fillDataIntoSelect(dataListNationality, `Please Select Client's Country`, nationalityListEdit, 'countryname', inquiry.nationality.countryname);
    // InqClientNationality

    inqStatusList = ajaxGetRequest('/inqstatus/alldata')
    fillDataIntoSelect(inqStatus, 'Select Status', inqStatusList, 'name', inquiry.inquirystatus.name);

    //inquiry eka manual damma ekak neme nam me fields employeelata edit karanna bari karanna hadanna💥 fields tika danna
    if (inquiry.recievedmethod == "Website") {
        inqMainEnquiry.disabled = true;
        inqRecievedMethod.disabled = true;
        inqType.disabled = true;
        inqRecievedDate.disabled = true;
        // inqRecievedTime.disabled = true;
        inqInterestedPkg.disabled = true;
    }

    //add button should be disabled in EDIT MODE
    inqManualAddBtn.disabled = true;
    inqManualAddBtn.style.cursor = "not-allowed";

    inqManualUpdateBtn.disabled = false;
    inqManualUpdateBtn.style.cursor = "pointer";

}

const updateManualInq = () => {

    let errors = checkManualInqErrors();;
    if (errors == '') {
        let updates = getInquiryUpdates();
        if (updates == '') {
            alert('No Changes Detected');
        } else {
            let userResponse = confirm("Sure To Update ? \n \n " + updates);
            if (userResponse) {
                let putServiceResponce = ajaxRequest("/inquiry", "PUT", inquiry);

                if (putServiceResponce == "OK") {
                    alert("Successfully Updted");
                    $('#modalInquiry').modal('hide');
                    refreshInquiryTable();
                    formManualInquiry.reset();
                    refreshInquiryForm();

                } else {
                    alert("An Error Occured " + putServiceResponce);
                }
            } else {
                alert('Operator Cancelled The Task')
            }
        }
    } else {
        alert('form has following errors \n ' + errors);
    }
}

const getInquiryUpdates = () => {
    let updates = '';

    if (inquiry.inqtype != oldInquiry.inqtype) {
        updates = updates + " Inquiry Type has changed \n";
    }

    if (oldInquiry.based_tpkg_id != null) {
        if (inquiry.based_tpkg_id.packagename != oldInquiry.based_tpkg_id.packagename) {
            updates = updates + " Interested Package has changed \n";
        }
    }

    if (inquiry.recievedmethod != oldInquiry.recievedmethod) {
        updates = updates + " Inquiry source has changed \n";
    }

    if (inquiry.recieveddate != oldInquiry.recieveddate) {
        updates = updates + " Recieved date has changed \n";
    }

    if (inquiry.recievedtime != oldInquiry.recievedtime) {
        updates = updates + "  Recieved time has changed \n";
    }

    if (inquiry.inqlocaladultcount != oldInquiry.inqlocaladultcount) {
        updates = updates + "Local Adult Traveller's count has changed \n";
    }

    if (inquiry.inqlocalchildcount != oldInquiry.inqlocalchildcount) {
        updates = updates + "Local Child Traveller's count has changed \n";
    }

    if (inquiry.inqforeignadultcount != oldInquiry.inqforeignadultcount) {
        updates = updates + "Foreign Adult Traveller's count has changed \n";
    }

    if (inquiry.inqforeignchildcount != oldInquiry.inqforeignchildcount) {
        updates = updates + "Foreign Child Traveller's count has changed \n";
    }

    if (inquiry.enquiry != oldInquiry.enquiry) {
        updates = updates + " Main Inquiry Text has changed \n";
    }

    if (inquiry.arrivaldate != oldInquiry.arrivaldate) {
        updates = updates + " Client arrival date has changed \n";
    }

    if (inquiry.departuredate != oldInquiry.departuredate) {
        updates = updates + " Client departure date has changed \n";
    }

    if (inquiry.pickuplocation != oldInquiry.pickuplocation) {
        updates = updates + " Pickup location has changed \n";
    }

    if (inquiry.dropofflocation != oldInquiry.dropofflocation) {
        updates = updates + " Dropoff location has changed \n";
    }

    if (inquiry.clientname != oldInquiry.clientname) {
        updates = updates + " Client's Name has changed \n";
    }

    if (inquiry.nationality.countryname != oldInquiry.nationality.countryname) {
        updates = updates + " Client's Nationality has changed \n";
    }

    if (inquiry.passportnumornic != oldInquiry.passportnumornic) {
        updates = updates + " Client's  NIC/Passport number has changed \n";
    }

    if (inquiry.email != oldInquiry.email) {
        updates = updates + " Client's Email has changed \n";
    }

    if (inquiry.contactnum != oldInquiry.contactnum) {
        updates = updates + " Client's Contact #1 has changed \n";
    }

    if (inquiry.contactnumtwo != oldInquiry.contactnumtwo) {
        updates = updates + " Client's Contact #2 has changed \n";
    }

    if (inquiry.prefcontactmethod != oldInquiry.prefcontactmethod) {
        updates = updates + " Preferred contact method has changed \n";
    }

    if (inquiry.inquirystatus.name != oldInquiry.inquirystatus.name) {
        updates = updates + " Inquiry Status has changed \n";
    }

    if (inquiry.note != oldInquiry.note) {
        updates = updates + " Note has changed \n";
    }

    return updates;
}

const deleteInquiry = () => {

    const userConfirm = confirm("You Sure To Delete?")
    if (userConfirm) {
        let deleteServerResponse = ajaxRequest("/inquiry", "DELETE", ob)
        if (deleteServerResponse == "OK") {
            alert('Deleted succesfully');
            refreshInquiryTable();
        }
    } else {
        alert("Delete Failed \n" + deleteServerResponse);
    }
}


const filterClient = () => {
    let nicorppt = "";
    let nationalityVar = inquiry.nationality.countryname;
    console.log(nationalityVar);
    if (nationalityVar == "Sri Lanka") {
        if (new RegExp("^([0-9]{9}[VvXx])||([0-9]{12})$").test(inqClientPassportNumorNIC.value)) {
            nicorppt = inqClientPassportNumorNIC.value;
            console.log(nicorppt);
        }
    } else {
        if (new RegExp("^[A-Z0-9]{6,9}$").test(inqClientPassportNumorNIC.value)) {
            nicorppt = inqClientPassportNumorNIC.value;
        }
    }

    if (nicorppt != "") {
        let extClient = ajaxGetRequest("/client/byclietnicorppt/" + nicorppt);
        console.log(extClient);
        if (extClient != "") {

            inqClientName.value = extClient.clientname;
        }
    }

}

