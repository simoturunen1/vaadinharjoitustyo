package com.example.application.views;

import com.example.application.data.User;
import com.example.application.security.AuthenticatedUser;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

    private final AuthenticatedUser authenticatedUser;
    private final AccessAnnotationChecker accessChecker;

    private H1 viewTitle;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();

        UI.getCurrent().getPage().executeJs("""
            const root = document.documentElement;
            const keys = ['taustavari', 'tekstivari', 'painike-tausta', 'painike-teksti', 'fontti'];
            keys.forEach(k => {
                const val = localStorage.getItem('teema-' + k);
                if (val) {
                    const cssVar = '--' + k.replaceAll('-', '');
                    root.style.setProperty(cssVar, val);
                }
            });
        """);
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.setId("page-title");
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        Span appName = new Span("VaadinHarjoitustyo");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);

        Header header = new Header(appName);
        Scroller scroller = new Scroller(createNavigation());

        appName.addClassName("custom-nav");
        header.addClassName("custom-nav");
        scroller.addClassName("custom-nav");

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
        nav.addClassName("custom-nav");

        List<MenuEntry> menuEntries = MenuConfiguration.getMenuEntries();
        menuEntries.forEach(entry -> {
            if (entry.icon() != null) {
                nav.addItem(new SideNavItem(entry.title(), entry.path(), new SvgIcon(entry.icon())));
            } else {
                nav.addItem(new SideNavItem(entry.title(), entry.path()));
            }
        });

        return nav;
    }



    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());
            StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(user.getProfilePicture()));
            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            userMenu.addClassName("custom-nav");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.Gap.SMALL);
            userName.add(div);
            userName.getSubMenu().addItem("Kirjaudu ulos", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Kirjaudu sisään");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        return MenuConfiguration.getPageHeader(getContent()).orElse("");
    }
}
