package com.github.tachesimazzoca.spring.web;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping(value = "/basics")
public class BasicsController {
    @RequestMapping(
        value = "/plain"
      , method = RequestMethod.GET
      , produces = "text/plain; charset=utf-8")
    @ResponseBody
    public String plain(Model model) {
        return "<strong>Hello</strong>";
    }

    @RequestMapping("/headers")
    public ResponseEntity<String> headers(HttpEntity<byte[]> requestEntity)
            throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("<request>");
        sb.append("<headers>");

        Set<Map.Entry<String, List<String>>> entrySet = requestEntity.getHeaders().entrySet();
        for (Map.Entry<String, List<String>> entry : entrySet) {
            sb.append("<header>");
            sb.append("<key>");
            sb.append(StringEscapeUtils.escapeXml(entry.getKey()));
            sb.append("</key>");
            sb.append("<values>");
            for (String v : entry.getValue()) {
                sb.append("<value>");
                sb.append(StringEscapeUtils.escapeXml(v));
                sb.append("</value>");
            }
            sb.append("</values>");
            sb.append("</header>");
        }
        sb.append("</headers>");

        sb.append("<body>");
        sb.append(StringEscapeUtils.escapeXml(
            new String(requestEntity.getBody(), "UTF-8")));
        sb.append("</body>");

        sb.append("</request>");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/xml");
        return new ResponseEntity<String>(
            sb.toString(), responseHeaders, HttpStatus.OK);
    }
}
