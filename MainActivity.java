package com.sahilnow.app;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends android.app.Activity {
    private int dp(float value) { return (int) (value * getResources().getDisplayMetrics().density + 0.5f); }

    private TextView tv(String text, float sizeSp, int color) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(sizeSp);
        t.setTextColor(color);
        t.setFontFeatureSettings("kern");
        return t;
    }

    private GradientDrawable rounded(int color, float radiusDp, int strokeColor, float strokeDp) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radiusDp));
        if (strokeDp > 0) d.setStroke(dp(strokeDp), strokeColor);
        return d;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setStatusBarColor(Color.TRANSPARENT);
        w.setNavigationBarColor(Color.rgb(247,250,255));
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            w.setDecorFitsSystemWindows(false);
            WindowInsetsController c = w.getInsetsController();
            if (c != null) c.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        }
        buildUi();
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundResource(com.sahilnow.app.R.drawable.bg_gradient);
        root.setPadding(dp(18), dp(18), dp(18), dp(12));

        TextView top = tv("SahilNow", 30, Color.rgb(16,32,51));
        top.setTypeface(null, android.graphics.Typeface.BOLD);
        top.setPadding(dp(2), dp(24), 0, 0);
        root.addView(top, new LinearLayout.LayoutParams(-1, dp(52)));

        TextView sub = tv("ServiceNow map · modules & important tables", 14, Color.rgb(102,117,138));
        root.addView(sub, new LinearLayout.LayoutParams(-1, dp(34)));

        LinearLayout badgeRow = new LinearLayout(this);
        badgeRow.setGravity(Gravity.CENTER_VERTICAL);
        TextView offline = tv("OFFLINE", 11, Color.rgb(31,122,224));
        offline.setGravity(Gravity.CENTER);
        offline.setTypeface(null, android.graphics.Typeface.BOLD);
        offline.setBackgroundResource(com.sahilnow.app.R.drawable.pill_bg);
        offline.setPadding(dp(12), dp(7), dp(12), dp(7));
        badgeRow.addView(offline, new LinearLayout.LayoutParams(dp(84), dp(32)));
        TextView hint = tv("Tap a module to reveal tables", 12, Color.rgb(102,117,138));
        hint.setPadding(dp(12), 0, 0, 0);
        badgeRow.addView(hint);
        root.addView(badgeRow, new LinearLayout.LayoutParams(-1, dp(44)));

        ScrollView scroll = new ScrollView(this);
        scroll.setClipToPadding(false);
        scroll.setPadding(0, dp(8), 0, dp(18));

        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        list.setPadding(0, 0, 0, dp(30));

        Map<String, List<String>> modules = data();
        int index = 1;
        for (Map.Entry<String, List<String>> e : modules.entrySet()) {
            list.addView(moduleCard(index++, e.getKey(), e.getValue()));
        }

        TextView footer = tv("Prototype 0.1 · static reference only", 11, Color.rgb(116,130,150));
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(10), 0, 0);
        list.addView(footer, new LinearLayout.LayoutParams(-1, dp(38)));
        scroll.addView(list);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        setContentView(root);
    }

    private View moduleCard(int number, String title, List<String> tables) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(15), dp(16), dp(12));
        card.setBackgroundResource(com.sahilnow.app.R.drawable.module_card);
        card.setElevation(dp(2));

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView num = tv(String.format("%02d", number), 11, Color.rgb(31,122,224));
        num.setGravity(Gravity.CENTER);
        num.setTypeface(null, android.graphics.Typeface.BOLD);
        num.setBackground(rounded(0xFFF0F7FF, 12, 0x00000000, 0));
        header.addView(num, new LinearLayout.LayoutParams(dp(36), dp(36)));

        LinearLayout titleBox = new LinearLayout(this);
        titleBox.setOrientation(LinearLayout.VERTICAL);
        titleBox.setPadding(dp(12), 0, dp(8), 0);
        TextView titleView = tv(title, 16, Color.rgb(16,32,51));
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        TextView count = tv(tables.size() + " important tables", 11, Color.rgb(102,117,138));
        titleBox.addView(titleView);
        titleBox.addView(count);
        header.addView(titleBox, new LinearLayout.LayoutParams(0, -2, 1));

        TextView chevron = tv("⌄", 22, Color.rgb(76,95,118));
        chevron.setGravity(Gravity.CENTER);
        header.addView(chevron, new LinearLayout.LayoutParams(dp(32), dp(40)));
        card.addView(header);

        LinearLayout tablesBox = new LinearLayout(this);
        tablesBox.setOrientation(LinearLayout.VERTICAL);
        tablesBox.setVisibility(View.GONE);
        tablesBox.setPadding(dp(48), dp(10), 0, dp(3));
        for (String table : tables) {
            TextView row = tv(table, 13, Color.rgb(35,54,76));
            row.setPadding(dp(12), dp(10), dp(10), dp(10));
            row.setBackgroundResource(com.sahilnow.app.R.drawable.table_bg);
            LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, dp(40));
            rp.bottomMargin = dp(6);
            tablesBox.addView(row, rp);
        }
        card.addView(tablesBox);

        View.OnClickListener toggle = v -> {
            boolean open = tablesBox.getVisibility() == View.VISIBLE;
            tablesBox.setVisibility(open ? View.GONE : View.VISIBLE);
            chevron.setText(open ? "⌄" : "⌃");
        };
        header.setOnClickListener(toggle);
        titleView.setOnClickListener(toggle);

        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2);
        cp.bottomMargin = dp(12);
        return cardWithMargin(card, cp);
    }

    private View cardWithMargin(View v, LinearLayout.LayoutParams p) {
        LinearLayout wrap = new LinearLayout(this);
        wrap.setOrientation(LinearLayout.VERTICAL);
        wrap.addView(v, p);
        return wrap;
    }

    private Map<String, List<String>> data() {
        Map<String, List<String>> m = new LinkedHashMap<>();
        m.put("Platform & Core", Arrays.asList("task", "sys_user", "sys_user_group", "sys_user_has_role", "sys_dictionary", "sys_db_object", "sys_properties", "sys_attachment", "sys_journal_field", "sys_choice"));
        m.put("IT Service Management (ITSM)", Arrays.asList("incident", "problem", "change_request", "change_task", "sc_request", "sc_req_item", "sc_task", "sc_cat_item", "sc_category", "task_sla"));
        m.put("Service Catalog & Request", Arrays.asList("sc_request", "sc_req_item", "sc_task", "sc_cat_item", "sc_cat_item_producer", "sc_category", "item_option_new", "sc_item_option", "sc_item_option_mtom", "sc_requested_item"));
        m.put("Knowledge Management", Arrays.asList("kb_knowledge", "kb_knowledge_base", "kb_category", "kb_feedback_task", "kb_use", "kb_feedback"));
        m.put("Configuration Management / CMDB", Arrays.asList("cmdb_ci", "cmdb_rel_ci", "cmdb_ci_server", "cmdb_ci_computer", "cmdb_ci_linux_server", "cmdb_ci_win_server", "cmdb_ci_network", "cmdb_ci_database", "cmdb_ci_service", "cmdb_ci_business_app"));
        m.put("IT Operations Management (ITOM)", Arrays.asList("em_alert", "em_event", "cmdb_ci", "cmdb_rel_ci", "sa_classification", "cmdb_health_metric", "cmdb_ci_service", "cmdb_ci_endpoint"));
        m.put("IT Asset Management (ITAM)", Arrays.asList("alm_asset", "alm_hardware", "alm_license", "alm_license_usage", "alm_entitlement", "alm_transfer_order", "alm_stockroom", "alm_asset_consumable", "alm_vendor"));
        m.put("Customer Service Management (CSM)", Arrays.asList("sn_customerservice_case", "customer_account", "customer_contact", "consumer", "sn_customerservice_task", "sn_customerservice_product", "sn_customerservice_communication"));
        m.put("HR Service Delivery (HRSD)", Arrays.asList("sn_hr_core_case", "sn_hr_core_task", "sn_hr_core_profile", "sn_hr_core_service", "sn_hr_core_hr_service", "sn_hr_core_topic", "sn_hr_integrations_inbound_case"));
        m.put("Security Operations (SecOps)", Arrays.asList("sn_si_incident", "sn_si_task", "sn_vul_vulnerable_item", "sn_vul_vulnerability", "sn_vul_solution", "sn_ti_indicator", "sn_ti_observable"));
        m.put("Governance, Risk & Compliance (IRM)", Arrays.asList("sn_grc_issue", "sn_grc_profile", "sn_risk_risk", "sn_risk_statement", "sn_compliance_policy", "sn_compliance_control", "sn_compliance_control_objective", "sn_compliance_task"));
        m.put("Strategic Portfolio Management (SPM)", Arrays.asList("pm_project", "pm_project_task", "dmn_demand", "pm_portfolio", "pm_program", "rm_story", "rm_scrum_task", "pm_project_change_request"));
        m.put("Field Service Management (FSM)", Arrays.asList("wm_order", "wm_task", "wm_work_order", "wm_dispatch_queue", "wm_agent_workspace", "wm_shift", "wm_resource"));
        m.put("Application Development & App Engine", Arrays.asList("sys_app_application", "sys_scope", "sys_metadata", "sys_update_xml", "sys_atf_test", "sys_atf_test_suite", "sys_script", "sys_script_include", "sys_ui_action", "sys_ui_policy"));
        m.put("Integration & APIs", Arrays.asList("sys_rest_message", "sys_rest_message_fn", "sys_ws_definition", "sys_ws_operation", "sys_web_service", "ecc_queue", "sys_outbound_http_log", "sys_import_set", "sys_transform_map", "sys_import_set_row"));
        m.put("Notifications, Events & Automation", Arrays.asList("sysevent", "sysevent_email_action", "sys_email", "sys_trigger", "wf_context", "wf_activity", "sysauto_script", "sysauto_report"));
        return m;
    }
}
