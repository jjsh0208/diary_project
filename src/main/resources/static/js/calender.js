let today = new Date();
let thisDay = today.getDate();
let year = today.getFullYear();
let month = today.getMonth() + 1;
let date = today.getDay();
function getFirstDayIndex() {
    let fistDayIndex = new Date(`${year}-${month}-01`).getDay();
    return fistDayIndex;
}
let fistDayIndex = getFirstDayIndex();
let index = 1;

const calDic = {
    1: 31,
    2: 28,
    3: 31,
    4: 30,
    5: 31,
    6: 30,
    7: 31,
    8: 31,
    9: 30,
    10: 31,
    11: 30,
    12: 31,
};
const weekDic = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];
const weekDic2 = [
    "SUNDAY",
    "MONDAY",
    "TUESDAY",
    "WEDNESDAY",
    "THURSDAY",
    "FRIDAY",
    "SATDAY",
];
weekDic.map((dic) => {
    let container = document.querySelector("#weekDic");
    let newElement = document.createElement("div");
    newElement.id = dic;
    newElement.className = "dic";
    newElement.innerHTML = dic;
    container.appendChild(newElement);
});
var day = 1;
function makeFirstWeek() {
    day = 1;
    let firstWeek = [];
    i = 0;
    fistDayIndex = getFirstDayIndex();
    while (i < 7) {
        if (i < fistDayIndex) {
            firstWeek.unshift(".");
        } else {
            firstWeek.push(day);
            day += 1;
        }
        i += 1;
    }
    return firstWeek;
}

function makeCalendar() {
    let Calendar = [];
    let container = document.querySelector("#Calendar");
    container.innerHTML = "";
    Calendar.push(makeFirstWeek());
    let m = calDic[month * 1];
    let week = [];
    let nDay = 1;
    while (day <= m) {
        week.push(day);
        day += 1;
        if (week.length === 7) {
            Calendar.push(week);
            week = [];
        }
    }
    Calendar.push(week);
    Calendar.map((week) => {
        week.map((day) => {
            let newElement = document.createElement("div");

            newElement.className =
                day === today.getDate() && month === today.getMonth() + 1
                    ? "day today"
                    : "day";
            newElement.innerHTML = day;
            container.appendChild(newElement);
        });
    });
    return Calendar;
}

let Calendar = makeCalendar();

function handleWeek(opt) {
    document.getElementById(index).className = "imgview";
    if (opt > 0) {
        month += 1;
        if (month > 12) {
            month = 1;
            year += 1;
        }
        index += 1;
        index > 13 ? (index = 1) : index;
    } else {
        month -= 1;
        if (month < 1) {
            month = 12;
            year -= 1;
        }
        index -= 1;
        index < 1 ? (index = 13) : index;
    }
    console.log(year, month);
    console.log(index);
    makeCalendar();
    document.getElementById("year").innerHTML = "";
    document.getElementById("month").innerHTML = "";
    document.getElementById("year").innerHTML = year + " / ";
    document.getElementById("month").innerHTML = month;
    document.getElementById(index).className = "imgview opaque";
}

document.getElementById("year").innerHTML = year + " / ";
document.getElementById("month").innerHTML = month;
document.getElementById("date").innerHTML = weekDic2[date];