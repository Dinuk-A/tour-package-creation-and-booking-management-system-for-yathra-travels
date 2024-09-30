window.addEventListener('load', () => {

    //fill the package selection button
    pkgList = ajaxGetRequest('/tourpackageforweb/alldata')
    fillDataIntoSelect2(selectTPKGtoFilter, 'Filter Days By Web Packages', pkgList, 'packagecode', 'packagename');

    refreshDayPlanTable();

    refreshDayPlanForm();

    refreshNewLunchAddForm();

})

//to refresh main form
const refreshDayPlanForm = () => {

    dayplan = new Object();

    dayplan.vplaces = [];

    //get province list FOR STARTING POINT
    startProvinces = ajaxGetRequest("province/alldata");
    fillDataIntoSelect(dpStartProvinceSelect, 'Please Select The Province', startProvinces, 'name')

    //get province list FOR VISITING PLACES
    vpProvinces = ajaxGetRequest("province/alldata");
    fillDataIntoSelect(dpPlaceProvinceSelect, 'Please Select The Province', vpProvinces, 'name')

    //get province list FOR lunch hotels
    lunchHProvinces = ajaxGetRequest("province/alldata");
    fillDataIntoSelect(dpLunchProvinceSelect, 'Please Select The Province', lunchHProvinces, 'name')

    //get province list FOR ENDING POINTS 
    endProvinces = ajaxGetRequest("province/alldata");
    fillDataIntoSelect(dpEndProvinceSelect, 'Please Select The Province', endProvinces, 'name')

    //get inprogress or new inquiries
    inquiriesList = ajaxGetRequest("/inquiry/newandinprogressonly");
    fillDataIntoSelect(dpBasedInquiryNameSelect, 'Please Select The Inquiry', inquiriesList, 'inqcode')

    //get dp status 
    dpStatuses = ajaxGetRequest("/dpstatus/alldata")
    fillDataIntoSelect(dpStatusSelect, 'Select Status', dpStatuses, 'name', 'Active')
    dayplan.dpstatus_id = JSON.parse(dpStatusSelect.value);
    dpStatusSelect.style.border = "2px solid lime"

    //initially disable following elements
    dpPlaceDistrictSelect.disabled = true;
    allPlaces.disabled = true;
    dpStartDistrictSelect.disabled = true;
    dpEndDistrictSelect.disabled = true;
    dpStartStaySelect.disabled = true;
    dpEndStaySelect.disabled = true;

    //disable update btn
    dpUpdateBtn.disabled = true;
    dpUpdateBtn.style.cursor = "not-allowed";

    //disable save as new button
    dpSaveAsNewBtn.disabled = true;
    dpSaveAsNewBtn.style.cursor = "not-allowed";

    // show EMPTY district select tags, before filtered by province
    districts = [];
    fillDataIntoSelect(dpStartDistrictSelect, 'Please select the province first', districts, 'name');

    fillDataIntoSelect(dpEndDistrictSelect, 'Please select the province first', districts, 'name');

    fillDataIntoSelect(dpPlaceDistrictSelect, 'Please select the province first', districts, 'name');

    fillDataIntoSelect(dpLunchDistrictSelect, 'Please select the province first', districts, 'name');

    districts = ajaxGetRequest('/district/alldata')
    fillDataIntoSelect(selectLHDistrict, 'Please select District', districts, 'name');

    //show EMPTY accomadation tags before filtered by district
    stay = [];
    fillDataIntoSelect(dpStartStaySelect, 'Please select the district first', stay, 'name');

    fillDataIntoSelect(dpEndStaySelect, 'Please select the district first', stay, 'name');

    lunchHotels = [];
    fillDataIntoSelect(dpLunchHotelSelect, 'Please select the district first', lunchHotels, 'name')

    //fill the package selection button (on main window onload)
    pkgList = ajaxGetRequest('/tourpackageforweb/alldata')
    fillDataIntoSelect2(selectTPKGtoFilter, 'Filter Days By Web Packages', pkgList, 'packagecode', 'packagename');

    lhStatusesList = ajaxGetRequest("/lhstts/alldata");
    fillDataIntoSelect(lHStatusSelect, 'Please select Status', lhStatusesList, 'name')

}

//for 3 checkboxes
const selectDayType = (feild) => {

    dayplan.dayplancode = feild.value;

}

//for main table
const refreshDayPlanTable = () => {

    dayplans = ajaxGetRequest("/dayplan/alldata");

    const displayProperty =
        [
            { dataType: 'text', propertyName: 'dayplancode' },
            { dataType: 'text', propertyName: 'daytitle' },
            { dataType: 'function', propertyName: getDayType },
            { dataType: 'function', propertyName: getDayPlanStatus }

            // { dataType: 'button',  callback: (element) => {alert('Button clicked for element: ' + JSON.stringify(element));}  },

        ]

    fillDataIntoTable3(tableDayPlans, dayplans, displayProperty, buttonVisibility = true,)

    $('#tableDayPlans').dataTable();

}

//filter out day plans that only belongs to the given package
const refreshTableBasedOnGivenPkg = () => {

    //create an empty array
    dayListBasedOnPkg = []

    //get the json object of selected package
    let selectedPackage = JSON.parse(selectTPKGtoFilter.value);

    //get the start date of selected package
    let startDate = selectedPackage.sd_dayplan_id;
    dayListBasedOnPkg.push(startDate);

    //get all the middle days
    for (const midday of selectedPackage.tourpkgHasDaypPlanList) {
        dayListBasedOnPkg.push(midday.dayplan_id);
    }

    //get the end date of selected package
    if (selectedPackage.ed_dayplan_id != '') {
        let endDate = selectedPackage.ed_dayplan_id;
        dayListBasedOnPkg.push(endDate);
    }

    console.log('dayListBasedOnPkg', dayListBasedOnPkg);

    const displayProperty =
        [
            { dataType: 'text', propertyName: 'dayplancode' },
            { dataType: 'text', propertyName: 'daytitle' },
            { dataType: 'function', propertyName: getDayType },
            { dataType: 'function', propertyName: getDayPlanStatus }

        ]

    fillDataIntoTable3(tableDayPlans, dayListBasedOnPkg, displayProperty, buttonVisibility = true,)


}

// get starting point
// const getStartLocation = (ob) => {
//     if (ob.start_stay_id != null) {
//         return ob.start_stay_id.name
//     } else {
//         return ob.startlocation;
//     }
// }

const getDayType = (ob) => {
    if (ob.dpbasedinq != null) {
        return "<p style=' background-color: rgb(15, 236, 15) ;'>For Custom Package</p>"
    } else {
        return "<p style=' background-color: rgb(49 15 236 / 47%) ;'>For Website Package</p>"
    }

}

//if this day plan is for a package that display in website
const changesBasedDPInqBasedOrWeb = () => {

    if (forWebSite.checked) {

        dpBasedInquiryNameSelect.disabled = true
        dpBasedInquiryNameSelect.value = ''
        dayplan.dpbasedinq = null;

        lunchPlaceDivId.classList.add('d-none')
        endPointDivId.classList.add('d-none')
    } else {

        dpBasedInquiryNameSelect.disabled = false

        lunchPlaceDivId.classList.remove('d-none')
        endPointDivId.classList.remove('d-none')
    }
}

//changed based on base inq selection   
const bindBasedInq = () => {
    const selectedValue = dpBasedInquiryNameSelect.value
    dayplan.dpbasedinq = JSON.parse(selectedValue).inqcode;

    forWebSite.checked = false;
    forWebSite.disabled = true

}

//to  show places
const openDayPlanModal = (element) => {
    // const modalElementName = document.getElementById('modalElementName');
    // modalElementName.textContent = element.dayplancode;

    dayCodeHere.innerText = element.dayplancode;
    dayTitleHere.innerText = element.daytitle;

    modalBody = "";
    element.vplaces.forEach((place, index) => {
        modalBody = modalBody + "<p class='mb-0'>" + (parseInt(index) + 1) + " ⏩ " + place.name + "</p>";
    })
    vplacesListHere.innerHTML = modalBody;

    const modal = new bootstrap.Modal(document.getElementById('dayPlanModal'));
    modal.show();
}

// get status
const getDayPlanStatus = (ob) => {
    return ob.dpstatus_id.name;
}

//CALCULATE TOTAL TICKET COST AND VEHI PARKING FEE
const calcTktCost = (vpCostType, dpInputFieldID, dPPropertName) => {

    let cost = 0.00;
    dayplan.vplaces.forEach(placeObj => {
        fee = placeObj[vpCostType];
        cost = cost + fee;
        return cost;
    });

    dpInputFieldID.value = parseFloat(cost).toFixed(2);
    dayplan[dPPropertName] = dpInputFieldID.value;

}

//for add a single location
const addOne = () => {
    //check duplications when clicking add btn
    let selectedPlz = JSON.parse(allPlaces.value);
    let existPlz = false;

    for (const vplz of dayplan.vplaces) {
        if (selectedPlz.id == vplz.id) {
            existPlz = true;
            break;
        }
    }
    if (existPlz) {
        alert('this place is already selected')
    } else {
        let selectedPlace = JSON.parse(allPlaces.value);

        dayplan.vplaces.push(selectedPlace);
        fillDataIntoSelect(passedVPlaces, 'Selected Places', dayplan.vplaces, 'name')

        let existIndex = vplaces.map(place => place.name).indexOf(selectedPlace.name);
        if (existIndex != -1) {
            vplaces.splice(existIndex, 1)
        }
        fillDataIntoSelect(allPlaces, 'Please Select The Place', vplaces, 'name');

        //CALC TOTAL TKT COSTS AND VEHICLE PARKING COSTS 
        calcTktCost("feelocal", totalLocalAdultTktCost, "totalforeignadultticketcost");
        calcTktCost("feeforeign", totalForeignAdultTktCost, "totalforeignchildticketcost");
        calcTktCost("feechildlocal", totalLocalChildTktCost, "totallocaladultticketcost");
        calcTktCost("feechildforeign", totalForeignChildTktCost, "totallocalchildticketcost");

        calcTktCost("vehicleparkingfee", totalVehiParkingFeesInput, "vehiparkingfeeforday");
    }

}
//for add all locations 
const addAll = () => {
    for (const lvplz of vplaces) {
        let rightsideexistPlz = false;

        for (const rvplz of dayplan.vplaces) {
            if (lvplz.id == rvplz.id) {
                rightsideexistPlz = true;
                break;
            }
        }

        if (!rightsideexistPlz) {
            dayplan.vplaces.push(lvplz);
            fillDataIntoSelect(passedVPlaces, 'Selected Places', dayplan.vplaces, 'name')
        }
    }

    vplaces = [];
    fillDataIntoSelect(allPlaces, 'Please Select The Place', vplaces, 'name');

    //CALC TOTAL TKT COSTS AND VEHICLE PARKING COSTS
    calcTktCost("feelocal", totalLocalAdultTktCost, "totalforeignadultticketcost");
    calcTktCost("feeforeign", totalForeignAdultTktCost, "totalforeignchildticketcost");
    calcTktCost("feechildlocal", totalLocalChildTktCost, "totallocaladultticketcost");
    calcTktCost("feechildforeign", totalForeignChildTktCost, "totallocalchildticketcost");
    calcTktCost("vehicleparkingfee", totalVehiParkingFeesInput, "vehiparkingfeeforday");

}

//for remove a single location
const removeOne = () => {

    let selectedPlaceToRemove = JSON.parse(passedVPlaces.value);

    fillDataIntoSelect(allPlaces, 'Please Select The Place', vplaces, 'name');

    let existIndex = dayplan.vplaces.map(place => place.name).indexOf(selectedPlaceToRemove.name);  //dayplan.vplaces array eke thiynawada balanawa selected option eke name eka (selectedPlaceToRemove.name) ; passe eke index eka gannawa; 
    if (existIndex != -1) {
        dayplan.vplaces.splice(existIndex, 1)   //exist nam(ehema namak thiyanawa nam) right side eke list ekenma remove karanawa
    }

    fillDataIntoSelect(passedVPlaces, 'Selected Places', dayplan.vplaces, 'name');

    //CALC TOTAL TKT COSTS AND VEHICLE PARKING COSTS
    calcTktCost("feelocal", totalLocalAdultTktCost, "totalforeignadultticketcost");
    calcTktCost("feeforeign", totalForeignAdultTktCost, "totalforeignchildticketcost");
    calcTktCost("feechildlocal", totalLocalChildTktCost, "totallocaladultticketcost");
    calcTktCost("feechildforeign", totalForeignChildTktCost, "totallocalchildticketcost");

    calcTktCost("vehicleparkingfee", totalVehiParkingFeesInput, "vehiparkingfeeforday");

}

//for remove all locations
const removeAll = () => {

    dayplan.vplaces = [];
    fillDataIntoSelect(passedVPlaces, '', dayplan.vplaces, 'name');

    //CALC TOTAL TKT COSTS AND VEHICLE PARKING COSTS
    calcTktCost("feelocal", totalLocalAdultTktCost, "totalforeignadultticketcost");
    calcTktCost("feeforeign", totalForeignAdultTktCost, "totalforeignchildticketcost");
    calcTktCost("feechildlocal", totalLocalChildTktCost, "totallocaladultticketcost");
    calcTktCost("feechildforeign", totalForeignChildTktCost, "totallocalchildticketcost");

    calcTktCost("vehicleparkingfee", totalVehiParkingFeesInput, "vehiparkingfeeforday");

}

//get districts by province
const getDistByProvince = (provinceSelectid, districtSelectId) => {

    const currentProvinceID = JSON.parse(provinceSelectid.value).id;
    provinceSelectid.style.border = '2px solid lime';
    districtSelectId.disabled = false;
    const districts = ajaxGetRequest("district/getdistrictbyprovince/" + currentProvinceID);
    fillDataIntoSelect(districtSelectId, " Please Select The District ", districts, 'name');
}

//getvisiting places by district
const getVPlacesByDistrict = () => {
    const selectedDistrict = JSON.parse(dpPlaceDistrictSelect.value).id;
    dpPlaceDistrictSelect.style.border = '2px solid lime';
    allPlaces.disabled = false;
    vplaces = ajaxGetRequest("/attraction/bydistrict/" + selectedDistrict);
    fillDataIntoSelect(allPlaces, 'Please Select The Place', vplaces, 'name')
}

//get accomadations list by district
const getStayByDistrict = (DistSelectID, staySelectID) => {
    const selectedDistrict = JSON.parse(DistSelectID.value).id;
    DistSelectID.style.border = '2px solid lime';
    staySelectID.disabled = false;
    stayListByDistrict = ajaxGetRequest("/stay/bydistrict/" + selectedDistrict)
    fillDataIntoSelect(staySelectID, 'Please Select The Accomadation', stayListByDistrict, 'name')
}

//get lunch hotel by district
const getLunchHotelByDistrict = (DistSelectID, lhSelectID) => {
    const selectedDistrict = JSON.parse(DistSelectID.value).id;
    DistSelectID.style.border = '2px solid lime';
    lhSelectID.disabled = false;
    lunchHotelListByDistrict = ajaxGetRequest("/lunchhotel/bydistrict/" + selectedDistrict)
    fillDataIntoSelect(lhSelectID, 'Please Select The Hotel', lunchHotelListByDistrict, 'name')
}

//for main Add button
const addDayPlan = () => {

    const errors = checkDayPlanErrors();

    if (errors == "") {
        const userConfirm = confirm('Are You Sure To Add ? \n' + dayplan.daytitle);
        if (userConfirm) {
            let postServiceResponse = ajaxRequest("/dayplan", "POST", dayplan);

            if (new RegExp("^[A-Z]{5}[0-9]{1,3}$").test(postServiceResponse)) {
                alert("Succesfully Saved !!! \n " + postServiceResponse);
                formDayPlan.reset();
                refreshDayPlanForm();
                refreshDayPlanTable();
                $('#mainDayPlanFormModal').modal('hide');

            } else {
                alert("An Error Occured " + postServiceResponse);
            }
        } else {
            alert('Operation cancelled by the user')
        }
    } else {
        alert("form has errors \n \n" + errors)
    }

}

//save as a new dayplan record
const saveAsNewDayPlan = () => {

    let postServiceResponse = ajaxRequest("/dayplan/saveasnew", "POST", dayplan);
    if (new RegExp("^[A-Z]{5}[0-9]{1,3}$").test(postServiceResponse)) {
        alert("Succesfully Saved ! \n New Code : " + postServiceResponse);
        formDayPlan.reset();
        refreshDayPlanForm();
        refreshDayPlanTable();

        $('#mainDayPlanFormModal').modal('hide');
    } else {
        alert("An Error Occured " + postServiceResponse);
    }

}

//  finalPrice = Math.ceil(finalPrice / 1000) * 1000;.
//to check errors
const checkDayPlanErrors = () => {
    let errors = "";

    if (dayplan.dayplancode == null) {
        errors = errors + " Please choose The day type;in order to generate a code \n";
    }

    if (dayplan.daytitle == null) {
        errors = errors + " Please Enter The Title \n";
    }

    if (dayplan.kmcountforday == null) {
        errors = errors + " Please Enter The KM Count For Today(Use Google Earth) \n";
    }

    if (dayplan.dpstatus_id == null) {
        errors = errors + " Please Enter The Status \n";
    }

    if (dayplan.vplaces.length == 0) {
        errors = errors + " Please Enter Visiting Places \n";
    }

    if (dayplan.startlocation == null && dayplan.start_stay_id == null) {
        errors = errors + " Please Enter At Least One Starting Point \n";
    }


    if (!forWebSite.checked) {

        if (dayplan.endlocation == null && dayplan.end_stay_id == null) {
            errors = errors + " Please Enter At Least One Ending Point \n";
        }

        if (dayplan.lunch_hotel_id == null) {
            errors = errors + " Please Choose A Lunch Restaurant \n";
        }
    }

    return errors;
}

//for edit button (refill)
const dpFormReFill = (obj) => {
    dayplan = JSON.parse(JSON.stringify(obj));
    oldDayPlanObj = JSON.parse(JSON.stringify(obj));

    firstDayCB.disabled = true;
    middleDayCB.disabled = true;
    lastDayCB.disabled = true;

    if (dayplan.dpbasedinq == null) {
        forWebSite.checked = true;
        dpBasedInquiryNameSelect.disabled = true

        lunchPlaceDivId.classList.add('d-none')
        endPointDivId.classList.add('d-none')

    } else if (dayplan.dpbasedinq != null) {

        dpBasedInquiryNameSelect.value = dayplan.dpbasedinq;

        lunchPlaceDivId.classList.remove('d-none')
        endPointDivId.classList.remove('d-none')
    }

    if (oldDayPlanObj.lunch_hotel_id != null) {
        lunchHotels = ajaxGetRequest("/lunchhotel/alldata");
        fillDataIntoSelect(dpLunchHotelSelect, 'Please Select', lunchHotels, 'name', dayplan.lunch_hotel_id.name);
    }

    //refill end stay
    if (oldDayPlanObj.end_stay_id != null) {
        endStay = ajaxGetRequest("stay/alldata");
        fillDataIntoSelect(dpEndStaySelect, 'Please Select The Stay', endStay, 'name', dayplan.end_stay_id.name);

        //get province list FOR ENDING POINTS 
        endProvinces = ajaxGetRequest("province/alldata");
        fillDataIntoSelect(dpEndProvinceSelect, 'Please Select The Province', endProvinces, 'name', dayplan.end_district_id.province_id.name)

        //refill end district
        districts = ajaxGetRequest("district/alldata");
        fillDataIntoSelect(dpEndDistrictSelect, 'Please Select The District', districts, 'name', dayplan.end_district_id.name)
        dpEndDistrictSelect.disabled = false;

    }

    if (dayplan.endlocation != null) {
        endLocationText.value = dayplan.endlocation;
    }

    dpStartStaySelect.disabled = false;
    dpEndStaySelect.disabled = false;

    if (dayplan.dayplancode.substring(0, 2) == "FD") {
        firstDayCB.checked = true;

    } else if (dayplan.dayplancode.substring(0, 2) == "MD") {
        middleDayCB.checked = true;

    } else {
        lastDayCB.checked = true;
    }

    dpTitle.value = dayplan.daytitle;
    dpCode.value = dayplan.dayplancode;
    startLocationText.value = dayplan.startlocation;
    dpNote.value = dayplan.note;
    totalKMcount.value = dayplan.kmcountforday;

    //Refill province list FOR STARTING POINT
    startProvinces = ajaxGetRequest("province/alldata");
    fillDataIntoSelect(dpStartProvinceSelect, 'Please Select The Province', startProvinces, 'name', dayplan.start_district_id.province_id.name)

    //refill start district
    districts = ajaxGetRequest("district/alldata");
    fillDataIntoSelect(dpStartDistrictSelect, 'Please Select The District', districts, 'name', dayplan.start_district_id.name)
    dpStartDistrictSelect.disabled = false;

    //REFILL PASSED VPLACES
    fillDataIntoSelect(passedVPlaces, 'Please Select The Place', dayplan.vplaces, 'name')

    //refill start stay
    if (dayplan.start_stay_id != null) {
        startStay = ajaxGetRequest("stay/alldata");
        fillDataIntoSelect(dpStartStaySelect, 'Please Select The Stay', startStay, 'name', dayplan.start_stay_id.name)
    }

    //Refill dp status 
    dpStatuses = ajaxGetRequest("/dpstatus/alldata")
    fillDataIntoSelect(dpStatusSelect, 'Select Status', dpStatuses, 'name', dayplan.dpstatus_id.name);

    dpStartProvinceSelect.disabled = true;
    dpStartDistrictSelect.disabled = true;

    dpAddBtn.disabled = true;
    dpAddBtn.style.cursor = "not-allowed";

    dpUpdateBtn.disabled = false;
    dpUpdateBtn.style.cursor = "pointer";

    dpSaveAsNewBtn.disabled = true;
    dpSaveAsNewBtn.style.cursor = "not-allowed";

    $('#mainDayPlanFormModal').modal('show');

}

//pass inq code to day title
const passName = () => {

    let inqSelectElement = document.getElementById('dpBasedInquiryNameSelect');

    let selectedOption = JSON.parse(inqSelectElement.value);
    let selectedOpsCode = selectedOption.inqcode;

    // var selectedText = inqSelectElement.innerText;

    var inputField = document.getElementById('dpTitle');

    inputField.value += " for " + selectedOpsCode;

}

//to save the same day plan with minor changes
const duplicateDayPlan = (obj) => {
    dayplan = JSON.parse(JSON.stringify(obj));
    oldDayPlanObj = JSON.parse(JSON.stringify(obj));

    firstDayCB.disabled = true;
    middleDayCB.disabled = true;
    lastDayCB.disabled = true;

    if (dayplan.dpbasedinq == null) {
        forWebSite.checked = true;
        // dpBasedInquiryNameSelect.disabled = true
        // lunchPlaceDivId.classList.add('d-none')
        // endPointDivId.classList.add('d-none')

    }

    if (dpBasedInquiryNameSelect.value != null) {
        forWebSite.checked = true;
    }

    if (oldDayPlanObj.lunch_hotel_id != null) {
        lunchHotels = ajaxGetRequest("/lunchhotel/alldata");
        fillDataIntoSelect(dpLunchHotelSelect, 'Please Select', lunchHotels, 'name', dayplan.lunch_hotel_id.name);
    }

    //refill end stay
    if (oldDayPlanObj.end_stay_id != null) {
        endStay = ajaxGetRequest("stay/alldata");
        fillDataIntoSelect(dpEndStaySelect, 'Please Select The Stay', endStay, 'name', dayplan.end_stay_id.name);

        //get province list FOR ENDING POINTS 
        endProvinces = ajaxGetRequest("province/alldata");
        fillDataIntoSelect(dpEndProvinceSelect, 'Please Select The Province', endProvinces, 'name', dayplan.end_district_id.province_id.name)

        //refill end district
        districts = ajaxGetRequest("district/alldata");
        fillDataIntoSelect(dpEndDistrictSelect, 'Please Select The District', districts, 'name', dayplan.end_district_id.name)
        dpEndDistrictSelect.disabled = false;

    }

    if (dayplan.endlocation != null) {
        endLocationText.value = dayplan.endlocation;
    }

    dpStartStaySelect.disabled = false;
    dpEndStaySelect.disabled = false;

    if (dayplan.dayplancode.substring(0, 2) == "FD") {
        firstDayCB.checked = true;

    } else if (dayplan.dayplancode.substring(0, 2) == "MD") {
        middleDayCB.checked = true;

    } else {
        lastDayCB.checked = true;
    }

    dpTitle.value = dayplan.daytitle;
    dpCode.value = dayplan.dayplancode;
    startLocationText.value = dayplan.startlocation;
    dpNote.value = dayplan.note;
    totalKMcount.value = dayplan.kmcountforday;

    //Refill province list FOR STARTING POINT
    startProvinces = ajaxGetRequest("province/alldata");
    fillDataIntoSelect(dpStartProvinceSelect, 'Please Select The Province', startProvinces, 'name', dayplan.start_district_id.province_id.name)

    //refill start district
    districts = ajaxGetRequest("district/alldata");
    fillDataIntoSelect(dpStartDistrictSelect, 'Please Select The District', districts, 'name', dayplan.start_district_id.name)
    dpStartDistrictSelect.disabled = false;

    //REFILL PASSED VPLACES
    fillDataIntoSelect(passedVPlaces, 'Please Select The Place', dayplan.vplaces, 'name')

    //refill start stay
    if (dayplan.start_stay_id != null) {
        startStay = ajaxGetRequest("stay/alldata");
        fillDataIntoSelect(dpStartStaySelect, 'Please Select The Stay', startStay, 'name', dayplan.start_stay_id.name)
    }

    //Refill dp status 
    dpStatuses = ajaxGetRequest("/dpstatus/alldata")
    fillDataIntoSelect(dpStatusSelect, 'Select Status', dpStatuses, 'name', dayplan.dpstatus_id.name);

    dpStartProvinceSelect.disabled = true;
    dpStartDistrictSelect.disabled = true;

    dpAddBtn.disabled = true;
    dpAddBtn.style.cursor = "not-allowed";

    dpUpdateBtn.disabled = true;
    dpUpdateBtn.style.cursor = "not-allowed";

    dpSaveAsNewBtn.disabled = false;
    dpSaveAsNewBtn.style.cursor = "pointer";

    $('#mainDayPlanFormModal').modal('show');
}

//for check updates
const getDayPlanUpdates = () => {

    let updates = '';

    if (dayplan.daytitle != oldDayPlanObj.daytitle) {
        updates = updates + "Title has changed to " + dayplan.daytitle + "\n";
    }


    if (dayplan.startlocation != null) {
        updates = updates + "Start Location has changed to " + dayplan.startlocation + "\n";
    }


    if (dayplan.end_district_id != null) {
        updates = updates + "Ending District has changed to " + dayplan.end_district_id.name + "\n";
    }

    if (dayplan.end_stay_id != null) {
        updates = updates + "Ending Stay has changed to " + dayplan.end_stay_id.name + "\n";
    }

    if (dayplan.endlocation != null) {
        updates = updates + "End Location has changed to " + dayplan.endlocation + "\n";
    }

    if (dayplan.note != oldDayPlanObj.note) {
        updates = updates + "note has changed " + "\n";
    }

    if (dayplan.start_district_id != null) {
        updates = updates + "Starting District has changed to " + dayplan.start_district_id.name + "\n";
    }

    if (dayplan.lunch_hotel_id != null) {
        updates = updates + "Lunch Hotel has changed to " + dayplan.lunch_hotel_id.name + "\n";

    }

    if (dayplan.start_stay_id != null) {
        updates = updates + "Starting Stay has changed to " + dayplan.start_stay_id.name + "\n";
    }

    if (dayplan.dpstatus_id.name != oldDayPlanObj.dpstatus_id.name) {
        updates = updates + "Status has changed to " + dayplan.dpstatus_id.name + "\n";
    }

    if (dayplan.vplaces.length != oldDayPlanObj.vplaces.length) {
        updates = updates + "visiting places list changed";
    }

    return updates;
}

//for update btn
const updateDayPlan = () => {
    let errors = checkDayPlanErrors();
    if (errors == '') {
        let updates = getDayPlanUpdates();
        if (updates == '') {
            alert("No Changes Detected To Update");
        } else {
            let userConfirm = confirm("Are You Sure To Update?");
            if (userConfirm) {
                let putServiceResponce = ajaxRequest("/dayplan", "PUT", dayplan);
                if (putServiceResponce == "OK") {
                    alert("Successfully Updated");
                    $('#mainDayPlanFormModal').modal('hide');
                    refreshDayPlanTable();
                    formDayPlan.reset();
                    refreshDayPlanForm();

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

//for delete btn💥
const deleteDP = () => {
    const userConfirm = confirm('Are You Sure To Delete ?');

    if (userConfirm) {
        let deleteServerResponse = ajaxRequest("/dayplan", "DELETE", ob);

        if (deleteServerResponse == "OK") {
            alert("successfully Deleted");
            refreshDayPlanTable();
        } else {
            alert("An Error Occured \n" + deleteServerResponse);
        }
    } else {
        alert('Operator Cancelled The Task');
    }
}

const refreshNewLunchAddForm = () => {
    newLunchObj = new Object;
    inputLHPlaceName.value = "";
    inputCostperHead.value = "";
    // dpLunchNewPlaceAddDistrict.value = "";

    inputLHPlaceName.style.border = "2px solid #ced4da";
    inputCostperHead.style.border = "2px solid #ced4da";
    // dpLunchNewPlaceAddDistrict.style.border = "2px solid #ced4da";

    //💥💥💥thawa aluth ewa enna one, note,status
}

//add a new lunch place
const addNewLunchPlace = () => {

    if (newLunchObj.name != null && newLunchObj.costperhead != null) {
        let userConfirm = confirm('Are You Sure To Add?');
        if (userConfirm) {
            let postServiceResponse = ajaxRequest("/lunchhotel", "POST", newLunchObj);
            if (postServiceResponse == "OK") {
                alert('Successfully Saved');

                lunchHotels = ajaxGetRequest("/lunchhotel/alldata");
                fillDataIntoSelect(dpLunchHotelSelect, 'Please Select', lunchHotels, 'name', inputLHPlaceName.value)
                dayplan.lunch_hotel_id = JSON.parse(dpLunchHotelSelect.value)
                refreshNewLunchAddForm();
                $('#modalToAddNewLunchHotels').modal('hide');
                $('#mainDayPlanFormModal').modal('show');

            } else {
                alert('An Error Occured  \n' + postServiceResponse);
            }
        } else {
            alert('User Cancelled The Task')
        }
    } else {
        alert("Form Is Unfilled, Check Again")

    }

}


//total cost for today +++ discounted price
const calcTotalCostPerDay = () => {

    console.log('calcTotalCostPerDay fn running');

    let localAdultCost = parseInt(document.getElementById('totalLocalAdultTktCost').value);
    let ForeignAdultCost = parseInt(document.getElementById('totalForeignAdultTktCost').value);
    let LocalChildCost = parseInt(document.getElementById('totalLocalChildTktCost').value);
    let ForeignCgildCost = parseInt(document.getElementById('totalForeignChildTktCost').value);
    let ParkingCost = parseInt(document.getElementById('totalVehiParkingFeesInput').value);


    let totalCostForTodayVar = localAdultCost + ForeignAdultCost + LocalChildCost + ForeignCgildCost + ParkingCost;

    totalCostForToday.value = totalCostForTodayVar.toFixed(2);

    if (totalCostForTodayVar > 1000) {

        discountedPriceForToday.value = (totalCostForTodayVar * 0.9).toFixed(2);
        console.log('totalCostForTodayVar > 1000');

    } else if (totalCostForTodayVar <= 1000) {

        discountedPriceForToday.value = totalCostForTodayVar.toFixed(2);
        console.log('totalCostForTodayVar < 1000');

    }


}








