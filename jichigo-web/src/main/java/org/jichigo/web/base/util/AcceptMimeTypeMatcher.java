package org.jichigo.web.base.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AcceptMimeTypeMatcher {

    private List<MimeType> targetMimeTypes;

    public void setTargetAcceptMimeTypes(List<String> targetAcceptMimeTypes) {
        List<MimeType> list = new ArrayList<MimeType>();
        for (String targetAcceptMimeType : targetAcceptMimeTypes) {
            list.add(new MimeType(targetAcceptMimeType));
        }
        this.targetMimeTypes = list;
    }

    public boolean matches(String accept) {
        if (targetMimeTypes == null || targetMimeTypes.isEmpty()) {
            return true;
        }
        List<String> acceptEntries = Arrays.asList(accept.split(","));
        for (MimeType targetMimeType : targetMimeTypes) {
            for (String acceptEntry : acceptEntries) {
                MimeType acceptMimeType = new MimeType(acceptEntry);
                boolean typeMatch = "*".equals(acceptMimeType.getType())
                        || acceptMimeType.getType().equals(targetMimeType.getType());
                boolean subTypeMatch = "*".equals(acceptMimeType.getSubType())
                        || acceptMimeType.getSubType().equals(targetMimeType.getSubType());
                if (typeMatch && subTypeMatch) {
                    return true;
                }
            }
        }
        return false;
    }

    private class MimeType {
        private String type;
        private String subType;

        private MimeType(String acceptEntry) {
            String[] mimeSplitValues = acceptEntry.split(";")[0].split("/");
            this.type = mimeSplitValues[0];
            this.subType = mimeSplitValues[1];
        }

        private String getType() {
            return type;
        }

        private String getSubType() {
            return subType;
        }
    }
}
