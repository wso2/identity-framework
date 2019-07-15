<%--
  ~ Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
  ~
  ~  WSO2 Inc. licenses this file to you under the Apache License,
  ~  Version 2.0 (the "License"); you may not use this file except
  ~  in compliance with the License.
  ~  You may obtain a copy of the License at
  ~
  ~    http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an
  ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  ~ KIND, either express or implied.  See the License for the
  ~ specific language governing permissions and limitations
  ~ under the License.
  --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="org.wso2.carbon.identity.mgt.endpoint.IdentityManagementEndpointUtil" %>
<%@ page import="org.wso2.carbon.identity.mgt.endpoint.serviceclient.UserIdentityManagementAdminServiceClient" %>
<%@ page import="org.wso2.carbon.identity.mgt.stub.dto.ChallengeQuestionDTO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<jsp:directive.include file="localize.jsp"/>

<%
    boolean error = IdentityManagementEndpointUtil.getBooleanValue(request.getAttribute("error"));
    String errorMsg = IdentityManagementEndpointUtil.getStringValue(request.getAttribute("errorMsg"));

    Map<String, List<ChallengeQuestionDTO>> challengeQuestionSets = new HashMap<String, List<ChallengeQuestionDTO>>();

    if (session.getAttribute("username") != null) {
        UserIdentityManagementAdminServiceClient userIdentityManagementAdminServiceClient = new
                UserIdentityManagementAdminServiceClient();
        ChallengeQuestionDTO[] challengeQuestionDTOs =
                userIdentityManagementAdminServiceClient.getAllChallengeQuestions();

        for (ChallengeQuestionDTO challengeQuestionDTO : challengeQuestionDTOs) {
            String questionSetId = challengeQuestionDTO.getQuestionSetId();
            if (!challengeQuestionSets.containsKey(questionSetId)) {
                List<ChallengeQuestionDTO> questionDTOList = new ArrayList<ChallengeQuestionDTO>();
                challengeQuestionSets.put(questionSetId, questionDTOList);
            }
            challengeQuestionSets.get(questionSetId).add(challengeQuestionDTO);
        }

        session.setAttribute("challengeQuestionSet", challengeQuestionSets.keySet());
    } else {
        request.setAttribute("error", true);
        request.setAttribute("errorMsg", IdentityManagementEndpointUtil.i18n(recoveryResourceBundle,
                "Registered.user.not.found.in.session"));
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
%>

    <html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title><%=IdentityManagementEndpointUtil.i18n(recoveryResourceBundle, "Wso2.identity.server")%></title>

        <link rel="icon" href="images/favicon.png" type="image/x-icon"/>
        <link href="libs/bootstrap_3.3.5/css/bootstrap.min.css" rel="stylesheet">
        <link href="css/Roboto.css" rel="stylesheet">
        <link href="css/custom-common.css" rel="stylesheet">

        <!--[if lt IE 9]>
        <script src="js/html5shiv.min.js"></script>
        <script src="js/respond.min.js"></script>
        <![endif]-->
    </head>

    <body>

    <jsp:directive.include file="header.jsp"/>

    <!-- page content -->
    <div class="container-fluid body-wrapper">

        <div class="row">
            <!-- content -->
            <div class="col-xs-12 col-sm-10 col-md-8 col-lg-5 col-centered wr-login">
                <h2 class="wr-title uppercase blue-bg padding-double white boarder-bottom-blue margin-none">
                    <%=IdentityManagementEndpointUtil.i18n(recoveryResourceBundle, "Update.security.question")%>
                </h2>

                <div class="clearfix"></div>
                <div class="boarder-all ">

                    <% if (error) { %>
                    <div class="alert alert-danger" id="server-error-msg">
                        <%=IdentityManagementEndpointUtil.i18nBase64(recoveryResourceBundle, errorMsg)%>
                    </div>
                    <% } %>
                    <div class="alert alert-danger" id="error-msg" hidden="hidden"></div>

                    <div class="padding-double">
                        <form method="post" action="completeregistration.do" id="securityQuestionsForm">
                            <% for (Map.Entry<String, List<ChallengeQuestionDTO>> entry : challengeQuestionSets
                                    .entrySet()) {
                            %>
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 form-group">
                                <select name=<%="Q-" + Encode.forHtmlAttribute(entry.getKey())%>>
                                    <%
                                        for (ChallengeQuestionDTO challengeQuestionDTO : entry.getValue()) {
                                    %>
                                    <option value="<%=Encode.forHtmlAttribute(challengeQuestionDTO.getQuestion())%>">
                                        <%=Encode.forHtml(challengeQuestionDTO.getQuestion())%>
                                    </option>
                                    <%} %>
                                </select>

                            </div>
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 form-group required">
                                <label class="control-label">
                                    <%=IdentityManagementEndpointUtil.i18n(recoveryResourceBundle, "Your.answer")%>
                                </label>
                                <input type="text" name="<%="A-" + Encode.forHtmlAttribute(entry.getKey())%>"
                                       class="form-control"
                                       required/>
                            </div>
                            <%
                                }
                            %>

                            <div class="form-actions">
                                <table width="100%" class="styledLeft">
                                    <tbody>
                                    <tr class="buttonRow">
                                        <td>
                                            <button id="securityQuestionSubmit"
                                                    class="wr-btn grey-bg col-xs-12 col-md-12 col-lg-12 uppercase font-extra-large"
                                                    type="submit"><%=IdentityManagementEndpointUtil.i18n(recoveryResourceBundle,
                                                    "Update")%>
                                            </button>
                                        </td>
                                        <td>&nbsp;&nbsp;</td>
                                        <td>
                                            <button id="securityQuestionSkip"
                                                    class="wr-btn grey-bg col-xs-12 col-md-12 col-lg-12 uppercase font-extra-large"
                                                    onclick="location.href='../accountrecoveryendpoint/completeregistration.do?skip=true';">
                                                <%=IdentityManagementEndpointUtil.i18n(recoveryResourceBundle, "Skip")%>
                                            </button>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="clearfix"></div>
            </div>
            <!-- /content/body -->

        </div>
    </div>

    <!-- footer -->
    <footer class="footer">
        <div class="container-fluid">
            <p><%=IdentityManagementEndpointUtil.i18n(recoveryResourceBundle, "Wso2.identity.server")%> | &copy;
                <script>document.write(new Date().getFullYear());</script>
                <a href="<%=IdentityManagementEndpointUtil.i18n(recoveryResourceBundle, "business.homepage")%>" target="_blank"><i class="icon fw fw-wso2"></i> <%=
                IdentityManagementEndpointUtil.i18n(recoveryResourceBundle, "Inc")%></a>.
                <%=IdentityManagementEndpointUtil.i18n(recoveryResourceBundle, "All.rights.reserved")%>
            </p>
        </div>
    </footer>

    <script src="libs/jquery_1.11.3/jquery-1.11.3.js"></script>
    <script src="libs/bootstrap_3.3.5/js/bootstrap.min.js"></script>
    </body>
    </html>
