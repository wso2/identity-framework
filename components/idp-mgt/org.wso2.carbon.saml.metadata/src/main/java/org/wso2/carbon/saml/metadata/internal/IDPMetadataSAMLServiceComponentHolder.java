/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.saml.metadata.internal;

import org.wso2.carbon.saml.metadata.builder.IDPMetadataBuilder;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.ArrayList;
import java.util.List;

public class IDPMetadataSAMLServiceComponentHolder {

    public static IDPMetadataSAMLServiceComponentHolder instance = new IDPMetadataSAMLServiceComponentHolder();
    private RegistryService registryService;
    private RealmService realmService;
    private List<IDPMetadataBuilder> idpMetadataBuilderList;

    public static IDPMetadataSAMLServiceComponentHolder getInstance() {
        return instance;
    }

    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    public RegistryService getRegistryService() {
        return registryService;
    }

    public RealmService getRealmService() {
        return realmService;
    }

    public void setRealmService(RealmService realmService) {
        this.realmService = realmService;
    }

    public void addIdpMetadataBuilder(IDPMetadataBuilder idpMetadataBuilder){
        if(idpMetadataBuilderList == null){
            idpMetadataBuilderList = new ArrayList<IDPMetadataBuilder>();
        }
        idpMetadataBuilderList.add(idpMetadataBuilder);
    }

    public void removeIdpMetadataBuilder(IDPMetadataBuilder idpMetadataBuilder){
        idpMetadataBuilderList.remove(idpMetadataBuilder);
    }


}
