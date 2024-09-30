window.addEventListener('load', () => {
    refreshClientForm();
    refreshClientTable();
});

const refreshClientForm = () => {
    client = new Object();
    formClient.reset(); 

    nationalityList = ajaxGetRequest('/nationality/alldata')
    fillDataIntoSelect(clientNationality, 'Select Nationality', nationalityList, 'countryname');

    clientStatusList = ajaxGetRequest('/clientstatus/alldata')
    fillDataIntoSelect(clientStatus, 'Select Status', clientStatusList, 'name');

    clientName.style.border = "1px solid #ced4da";
    clientNationality.style.border = "1px solid #ced4da";
    clientPassportOrNIC.style.border = "1px solid #ced4da";
    clientEmail.style.border = "1px solid #ced4da";
    clientContactOne.style.border = "1px solid #ced4da";
    clientContactTwo.style.border = "1px solid #ced4da";
    clienAddressLocal.style.border = "1px solid #ced4da";
    clientNote.style.border = "1px solid #ced4da";
    clientStatus.style.border = "1px solid #ced4da";

    refreshClientTable();

    clientUpdateBtn.disabled = true;
    clientUpdateBtn.style.cursor = 'not-allowed';
}

const refreshClientTable = () => {

    clientsList = ajaxGetRequest("client/alldata")

    const displayProperty = [
        { dataType: 'text', propertyName: 'clientname' },
        { dataType: 'function', propertyName: getNationality },
        { dataType: 'text', propertyName: 'passportnumornic' },
        { dataType: 'text', propertyName: 'email' },
        { dataType: 'function', propertyName: getBothContactNums },
        { dataType: 'function', propertyName: getClientStatus },
    ]

    fillDataIntoTable3(clientMaintable, clientsList, displayProperty, buttonVisibility = true) //loggedUserPrivileges , disableButtonsCommonFn

    $('#clientMaintable').dataTable();
}

const getNationality = (ob) => {
    return ob.nationality.countryname;
}

const getBothContactNums = (ob) => {
    return ob.contactnum ;
}

const getClientStatus = (ob) => {
    return ob.clientstatus.name;
}

const addClient = () => {

    const errors = checkClientFormErrors();

    if (errors == '') {
        let userConfirm = confirm('are you sure ?');
        if (userConfirm) {
            let postServiceResponse = ajaxRequest("/client", "POST", client);

            if (postServiceResponse == "OK") {
                alert("Succesfully Saved !!!");
                refreshClientTable();
                formClient.reset();
                refreshClientForm();
                $('#modalClient').modal('hide');

            } else {
                alert("An Error Occured " + postServiceResponse);
            }
        } else {
            alert('Operation Cancelled By User')
        }
    } else {
        alert('form has following errors \n' + errors)
    }
}

const checkClientFormErrors = () => {
    let errors = '';
    if (client.clientname == null) {
        errors = errors + " Please Enter Client's Name \n";
    }

    if (client.nationality == null) {
        errors = errors + " Please Select Client's Nationaity \n";
    }

    if (client.passportnumornic == null) {
        errors = errors + " Please Enter Client's Passport or NIC number \n";
    }

    if (client.email == null) {
        errors = errors + " Please Enter Client's Email \n";
    }

    if (client.contactnum == null) {
        errors = errors + " Please Enter Client's Contact Number \n";
    }

    if (client.clientstatus == null) {
        errors = errors + " Please Select Client's Status \n";
    }

    return errors;
}

const editClient = (obj) => {

    client = JSON.parse(JSON.stringify(obj));
    clientOldObj = JSON.parse(JSON.stringify(obj));

    $('#modalClient').modal('show');

    nationalityList = ajaxGetRequest('/nationality/alldata')
    fillDataIntoSelect(clientNationality, 'Select Nationality', nationalityList, 'countryname', client.nationality.countryname);

    clientStatusList = ajaxGetRequest('/clientstatus/alldata')
    fillDataIntoSelect(clientStatus, 'Select Status', clientStatusList, 'name', client.clientstatus.name);

    clientName.value = client.clientname;
    clientPassportOrNIC.value = client.passportnumornic;
    clientEmail.value = client.contactnum;
    clientContactOne.value = client.email;
    clientContactTwo.value = client.contactnumtwo;
    clienAddressLocal.value = client.address;
    clientNote.value = client.note;

    clientAddBtn.disabled = true;
    clientAddBtn.style.cursor = 'not-allowed';

    clientUpdateBtn.disabled = false;
    clientUpdateBtn.style.cursor = 'pointer';

}

const updateClient = () => {

    let errors = checkClientFormErrors();
    if (errors == '') {
        let updates = getClientUpdates();
        if (updates == '') {
            alert('No Changes Detected');
        } else {
            let userResponse = confirm("Sure To Update ? \n \n " + updates);
            if (userResponse) {
                let putServiceResponce = ajaxRequest("/client", "PUT", attraction);

                if (putServiceResponce == "OK") {
                    alert("Successfully Updted");
                    $('#modalClient').modal('hide');
                    refreshClientTable();
                    formClient.reset();
                    refreshClientForm();

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

const getClientUpdates = () => {

    let updates = '';

    if (client.clientname != clientOldObj.clientname) {
        updates = updates + "Name Has Changed To " + client.clientname + "\n";
    }
    if (client.passportnumornic != clientOldObj.passportnumornic) {
        updates = updates + "Passport or NIC Has Changed To " + client.passportnumornic + "\n";
    }
    if (client.contactnum != clientOldObj.contactnum) {
        updates = updates + "Contact Num Has Changed To " + client.contactnum + "\n";
    }
    if (client.contactnumtwo != clientOldObj.contactnumtwo) {
        updates = updates + "contact num two Has Changed To " + client.contactnumtwo + "\n";
    }
    if (client.email != clientOldObj.email) {
        updates = updates + "email Has Changed To " + client.email + "\n";
    }
    if (client.address != clientOldObj.address) {
        updates = updates + "address Has Changed To " + client.address + "\n";
    }
    if (client.note != clientOldObj.note) {
        updates = updates + "note Has Changed To " + client.note + "\n";
    }
    if (client.nationality != clientOldObj.nationality) {
        updates = updates + "nationality Has Changed To " + client.nationality + "\n";
    }
    if (client.clientstatus != clientOldObj.name) {
        updates = updates + "clientstatus Has Changed To " + client.clientstatus + "\n";
    }

    return updates;
    /*




*/
}

const deleteClient = (ob, row) => {

    console.log('delete btn clicked');

    clientMaintable.children[1].children[row].style.backgroundColor = 'red';

    setTimeout(function () {
        const userConfirm = confirm('Are You Sure To Delete ?');

        if (userConfirm) {
            let deleteServerResponse = ajaxRequest("/client", "DELETE", ob);

            if (deleteServerResponse == "OK") {
                alert("successfully Deleted");
                refreshClientTable();
            } else {
                alert("An Error Occured \n" + deleteServerResponse);
            }
        } else {
            alert('Operator Cancelled The Task');
        }
    }, 300)
}

const changesBasedOnNationality = () => {

    if (client.nationality.countryname == "Srilanka") {
        clientPassportOrNIC.value = '';
        client.passportnumornic = null;
        clientPassportOrNIC.style.border = "1px solid #ced4da";
        labelForPPTorNIC.innerText = "NIC";
        addrRow.className = 'row mt-2';

    } else {
        clientPassportOrNIC.value = '';
        client.passportnumornic = null;
        clientPassportOrNIC.style.border = "1px solid #ced4da";
        labelForPPTorNIC.innerText = "PASSPROT";
        addrRow.className = 'row mt-2 d-none';

    }


}

const passCountryCode = () => {

    const selectedCountry = JSON.parse(clientNationality.value).countrycode;
    clientContactOne.value = selectedCountry;
    clientContactTwo.value = selectedCountry;

}

//define new validation fn for both nic and ppt
const validatorPptNoNIC = (fieldId, object, property) => {

    if (window[object].nationality.countryname == "Srilanka") {
        pattern = '^[1]{3}$';
    } else {
        pattern = '^[2]{3}$';
    }

    let regPattern = new RegExp(pattern);

    if (fieldId.value != "") {

        if (regPattern.test(fieldId.value)) {

            fieldId.style.border = "2px solid lime";

            window[object][property] = fieldId.value;
        } else {

            fieldId.style.border = "2px solid red";
            window[object][property] = null;
        }
    } else {
        window[object][property] = null;
        if (fieldId.required) {
            fieldId.style.border = "2px solid red";
        } else {
            fieldId.style.border = "2px solid #ced4da";
        }
    }
}
