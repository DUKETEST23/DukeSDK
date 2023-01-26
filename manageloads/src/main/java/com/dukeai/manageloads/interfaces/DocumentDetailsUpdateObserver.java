package com.dukeai.manageloads.interfaces;

import com.dukeai.manageloads.model.DocumentDetailsModel;

public interface DocumentDetailsUpdateObserver {
    void onChanged(String status, DocumentDetailsModel documentDetailsModel);
}
