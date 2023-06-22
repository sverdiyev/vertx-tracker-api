package com.sverdiyev.tracker.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Stock {

  String name;
  String ticker;
}
