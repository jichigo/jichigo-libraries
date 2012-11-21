package org.jichigo.web.base.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class AcceptMimeTypeMatcher {
    private static final Pattern ACCEPT_ENTRY_SPLIT_PATTERN = Pattern.compile(",");
    private static final Pattern TYPE_PARAMETER_SPLIT_PATTERN = Pattern.compile(";");
    private static final Pattern MIME_TYPE_SPLIT_PATTERN = Pattern.compile("/");
    private static final String WILD_CARD_CHAR = "*";

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
        List<String> acceptEntries = Arrays.asList(ACCEPT_ENTRY_SPLIT_PATTERN.split(accept));
        for (MimeType targetMimeType : targetMimeTypes) {
            for (String acceptEntry : acceptEntries) {
                MimeType acceptMimeType = new MimeType(acceptEntry);
                boolean typeMatch = WILD_CARD_CHAR.equals(acceptMimeType.getType())
                        || acceptMimeType.getType().equals(targetMimeType.getType());
                boolean subTypeMatch = WILD_CARD_CHAR.equals(acceptMimeType.getSubType())
                        || acceptMimeType.getSubType().equals(targetMimeType.getSubType());
                if (typeMatch && subTypeMatch) {
                    return true;
                }
            }
        }
        return false;
    }

    private class MimeType {
        private static final int SPLIT_INDEX_OF_TYPE = 0;
        private static final int SPLIT_INDEX_OF_SUBTYPE = 1;
        private String type;
        private String subType;

        private MimeType(String acceptEntry) {
            String mimeType = TYPE_PARAMETER_SPLIT_PATTERN.split(acceptEntry)[0];
            String[] mimeTypeSplitValues = MIME_TYPE_SPLIT_PATTERN.split(mimeType);
            this.type = mimeTypeSplitValues[SPLIT_INDEX_OF_TYPE];
            this.subType = mimeTypeSplitValues[SPLIT_INDEX_OF_SUBTYPE];
        }

        private String getType() {
            return type;
        }

        private String getSubType() {
            return subType;
        }
    }
}
