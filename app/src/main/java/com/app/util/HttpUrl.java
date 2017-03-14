package com.app.util;

/**
 * Created by masood on 8/11/15.
 */
public class HttpUrl {

   public static final String BASEURL = "http://122.175.50.252:8080/MedRepApplication";
    public static  final String COMMONURL = "http://122.175.50.252:8080/medrep-web";
    //Update url for live
  // public static final String BASEURL = "http://45.114.142.241:8080/MedRepApplication";
//    public static final String SIGNIN = "/oauth/token?grant_type=password&client_id=restapp&client_secret=restapp&username=umar.ashraf@gmail.com&password=Test123";
//    public static final String BASEURL = "http://medreplb-84253066.ap-southeast-1.elb.amazonaws.com";
//    public static final String
    public static final String DOCTOR_SIGN_UP = "/preapi/registration/signupDoctor";
    public static final String SIGNIN = "/oauth/token?grant_type=password&client_id=restapp&client_secret=restapp";
    public static final String REFRESHTOKEN ="/oauth/token?grant_type=refresh_token&client_id=restapp&client_secret=restapp&refresh_token=";
    public static final String DOCTOR_GET_MY_DETAILS = "/api/profile/getDoctorProfile?access_token=";
    public static final String GET_MY_ROLE = BASEURL + "/api/profile/getMyRole?access_token=";
//    public static final String FORGOTPASSWORD = "/preapi/registration/getNewSMSOTP/";
    public static final String FORGOT_PASSWORD = BASEURL + "/preapi/registration/forgotPassword";

    public static final String GET_NOTIFICATION_CONTENT = BASEURL + "/api/doctor/getMyNotificationContent/";

    public static final String UPDATE_NOTIFICATION = "/api/doctor/updateMyNotification?access_token=";

    public static final String NOTIFICATION_TYPE_URL = BASEURL + "/preapi/masterdata/getNotificationTypes";
    public static final String THERAPEUTIC_AREA_DETAILS_URL = BASEURL + "/preapi/masterdata/getTherapeuticAreaDetails";
    public static final String COMPANY_DETAILS_URL = BASEURL + "/preapi/masterdata/getCompanyDetails";

//    http://183.82.106.234:8080/MedRepApplication/preapi/registration/getNewSMSOTP/ssr@gmail.com/
//    public static final String IMAGE_UPLOAD ="preapi/registration/uploadDP";
    public static final String GET_PENDING_SURVEYS = "/api/doctor/getMyPendingSurveys?access_token=";
//    public static final String DOCTOR_APPOINTMENT = "/api/doctor/getMyAppointment";
    public static final String MYNOTIFICATION = BASEURL + "/api/doctor/getMyNotifications/";//{startDate}?access_token=
    public static final String OTP = "/preapi/registration/getOTP/";//number
    public static final String RESEND_OTP = "/preapi/registration/getNewSMSOTP/";//add registed email id here.
    public static final String UPDATE_DOCTOR_PROFILE = "/api/profile/updateDoctorProfile?access_token=";//access_token here

    public static final String DOCTOR_GET_DOCTOR_ACTIVITY_SCORE = BASEURL + "/api/analytic/getDoctorActivityScore?access_token=";//55bf6534-2880-4ad5-9519-f62dce2b754d


    public static final String VERIFICATION_OTP = "/preapi/registration/verifyMobileNo?";//token=34842&number=9247204720
    public static final String CREATE_APPOINTMENT ="/api/doctor/createAppointment?access_token=";
    public static final String UPDATE_APPOINTMENT = "/api/doctor/updateAppointment?access_token=";
    public static final String GET_APPOINTMENT = "/api/doctor/getMyAppointment/";




    public static final String UPLOAD_DP = "/preapi/registration/uploadDP";
    public static final String UPDATEPHARMA = BASEURL+"/api/profile/updatePharmaRepProfile?access_token=";

    // Pharma Registration url'// STOPSHIP: 07-10-2015

    public static final String url_resendOtp = BASEURL+"/preapi/registration/getNewSMSOTP/";
    public static final String url_pharmaReg = BASEURL+"/preapi/registration/signupRep";
    //http://183.82.106.234:8080/MedRepApplication/preapi/registration/signupRep
    public static final String PHARMA_GET_MY_DETAILS = "/api/profile/getPharmaRepProfile?access_token=";

    public static final String PHARMA_GET_MY_NOTIFICATIONS = BASEURL + "/api/pharmarep/getMyNotifications/";//{startDate}?access_token=";
    public static final String PHARMA_GET_NOTIFICATION_CONTENT = BASEURL + "/api/pharmarep/getMyNotificationContent/";//{detailNotificationId}?access_token=
    public static final String PHARMA_GET_NOTIFICATION_STATS = BASEURL + "/api/analytic/getNotificationStats/";//{notificationId}?access_token=20e20067-2f67-4d22-b405-f8039cc9f1b6"
    public static final String PHARMA_GET_APPOINTMENTS_BY_NOTIFICATION = BASEURL + "/api/analytic/getAppointmentsByNotificationId/";//{notificationId}?access_token=9f7a9cec-81a6-44a4-8ae6-
    public static final String PHARMA_GET_COMPANY_DOCTORS = BASEURL + "/api/profile/getMyCompanyDoctors?access_token=";//63ea69ca-58ab-488d-952b-603bbcbdabdf
    public static final String PHARMA_GET_MY_COMPANY = BASEURL + "/api/pharmarep/getMyCompany?access_token=";//4680b6ad-124f-47d9-8450-36078b3ada55
    public static final String PHARMA_GET_DOCTOR_PROFILE = BASEURL + "/api/profile/getDoctorProfile/";//{doctorId}?access_token=62fb897e-29a8-46bf-aa30-38f33252ae19
//    public static final String PHARMA_GET_NOTIFICATION_ACTIVITY_SCORE = BASEURL + "/api/analytic/getNotificationActivityScore/";//{notificationId}?access_token=20e20067-2f67-4d22-b405-f8039cc9f1b"
    //public static final String PHARMA_GET_NOTIFICATION_ACTIVITY_SCORE = BASEURL + "/api/analytic/getDoctorActivityScore/";//{doctorId}?access_token=20e20067-2f67-4d22-b405-f8039cc9f1b6
public static final String PHARMA_GET_NOTIFICATION_ACTIVITY_SCORE = BASEURL + "/api/analytic/getDoctorActivityByCompany/";//{doctorId}?access_token=20e20067-2f67-4d22-b405-f8039cc9f1b6

    public static final String PHARMA_GET_COMPANY_ACTIVITY_SCORE = BASEURL + "/api/analytic/getDoctorActivityByCompany/";//{doctorId}?access_token=20e20067-2f67-4d22-b405-f8039cc9f1b6
    public static final String PHARMA_GET_MY_TEAM = BASEURL + "/api/profile/getMyTeam?access_token=";//63ea69ca-58ab-488d-952b-603bbcbdabdf
    public static final String PHARMA_GET_PHARMA_REP_PROFILE = BASEURL + "/api/profile/getPharmaRepProfile/";//49?access_token=c7c3a2a1-981d-472f-8be8-774215f9518a
    public static final String PHARMA_GET_PHARMA_TEAM_PENDING_APPOINTMENTS = BASEURL + "/api/pharmarep/getMyTeamPendingAppointments?access_token=";//24b783b7-d8b4-449c-ba1a-84e64d8bedf8
    public static final String PHARMA_GET_MY_PENDING_APPOINTMENTS = BASEURL + "/api/pharmarep/getMyPendingAppointment?access_token=";//5717aa5f-5cac-4cca-af94-4da6ec434bf8
    public static final String PHARMA_GET_MY_UPCOMING_APPOINTMENTS = BASEURL + "/api/pharmarep/getMyUpcomingAppointment?access_token=";//8f3e5a25-848f-4ed3-a813-4c4008b0ac57
    public static final String PHARMA_GET_MY_COMPLETED_APPOINTMENTS = BASEURL + "/api/pharmarep/getMyCompletedAppointment?access_token=";//cd15d783-6b59-46cd-a47a-369481eec2e7
//    public static final String PHARMA_ACCEPT_APPOINTMENT = BASEURL + "/api/pharmarep/acceptAppointment/?access_token=";//60e6bfaa-0b61-42a4-92cd-3f71d3def6b2
    public static final String PHARMA_ACCEPT_APPOINTMENT = BASEURL + "/api/pharmarep/acceptAppointment?access_token=";//60e6bfaa-0b61-42a4-92cd-3f71d3def6b2

    public static final String PHARMA_GET_APPOINTMENTS_BY_REP = BASEURL + "/api/pharmarep/getAppointmentsByRep/";//{repId}?access_token=20e20067-2f67-4d22-b405-f8039cc9f1b6
    public static final String PHARMA_GET_DOCTOR_NOTIFICATION_STATS = BASEURL + "/api/analytic/getDoctorNotificationStats/";//{notificationId}?access_token=20e20067-2f67-4d22-b405-f8039cc9f1b6
    public static final String PHARMA_UPDATE_MY_NOTIFICATION = BASEURL + "/api/pharmarep/updateMyNotification?access_token=";//ab04ac82-6b19-492d-9e17-3e4fab387062"
    public static final String PHARMA_GET_NOTIFICATION_BY_ID = BASEURL + "/api/pharmarep/getNotificationById/";//{notificationId}?access_token=ab04ac82-6b19-492d-9e17-3e4fab387062

    public static final String PHARMA_UPDATE_APPOINTMENT_COMPLETE = BASEURL + "/api/pharmarep/updateAppointment?access_token=";//ab04ac82-6b19-492d-9e17-3e4fab387062"
    public static final String DOCTOR_APPOINTMENTS = BASEURL + "/api/doctor/getMyAppointment?access_token=";
    public static final String GET_MY_GROUPS = COMMONURL + "/getGroups?token=";
    public static final String CREATE_GROUP = COMMONURL + "/createGroup?token=";

    public static String displayPic = "";

    //http://183.82.106.234:8080/MedRepApplication/api/pharmarep/updateAppointment?access_token=20e20067-2f67-4d22-b405-f8039cc9f1b6
}
