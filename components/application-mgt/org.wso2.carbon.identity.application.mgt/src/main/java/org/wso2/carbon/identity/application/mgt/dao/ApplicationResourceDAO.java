/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.application.mgt.dao;

import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.Application;
import org.wso2.carbon.identity.application.common.model.ExtendedApplicationBasicInfo;

public interface ApplicationResourceDAO {

    ExtendedApplicationBasicInfo getExtendedApplicationBasicInfo(String applicationResourceId,
                                                                 String tenantDomain) throws IdentityApplicationManagementException;

    String createApplicationResource(Application application,
                                     String tenantDomain) throws IdentityApplicationManagementException;

    String createApplicationResource(Application application,
                                     String tenantDomain,
                                     String templateName) throws IdentityApplicationManagementException;

    Application getApplicationResource(String applicationResourceId,
                                       String tenantDomain) throws IdentityApplicationManagementException;

    void updateApplicationResource(String applicationResourceId,
                                           String tenantDomain,
                                           Application updatedApplication) throws IdentityApplicationManagementException;

    void deleteApplicationResource(String applicationResourceId,
                                           String tenantDomain) throws IdentityApplicationManagementException;
}
