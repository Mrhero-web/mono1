package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.Menu;
import com.ledar.mono.domain.enumeration.MenuType;
import com.ledar.mono.domain.enumeration.Status;
import com.ledar.mono.domain.enumeration.WebOrApp;
import com.ledar.mono.repository.MenuRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MenuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MenuResourceIT {

    private static final String DEFAULT_MENU_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MENU_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PARENT_ID = 1;
    private static final Integer UPDATED_PARENT_ID = 2;

    private static final String DEFAULT_PARENT_IDS = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_IDS = "BBBBBBBBBB";

    private static final Integer DEFAULT_TREE_SORT = 1;
    private static final Integer UPDATED_TREE_SORT = 2;

    private static final Integer DEFAULT_TREE_SORTS = 1;
    private static final Integer UPDATED_TREE_SORTS = 2;

    private static final Boolean DEFAULT_TREE_LEAF = false;
    private static final Boolean UPDATED_TREE_LEAF = true;

    private static final Integer DEFAULT_TREE_LEVEL = 1;
    private static final Integer UPDATED_TREE_LEVEL = 2;

    private static final String DEFAULT_TREE_NAMES = "AAAAAAAAAA";
    private static final String UPDATED_TREE_NAMES = "BBBBBBBBBB";

    private static final String DEFAULT_MENU_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MENU_NAME = "BBBBBBBBBB";

    private static final MenuType DEFAULT_MENU_TYPE = MenuType.MENU;
    private static final MenuType UPDATED_MENU_TYPE = MenuType.PERMISSION;

    private static final String DEFAULT_MENU_HREF = "AAAAAAAAAA";
    private static final String UPDATED_MENU_HREF = "BBBBBBBBBB";

    private static final String DEFAULT_MENU_ICON = "AAAAAAAAAA";
    private static final String UPDATED_MENU_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_MENU_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_MENU_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PERMISSION = "AAAAAAAAAA";
    private static final String UPDATED_PERMISSION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MENU_SORT = 1;
    private static final Integer UPDATED_MENU_SORT = 2;

    private static final Boolean DEFAULT_IS_SHOW = false;
    private static final Boolean UPDATED_IS_SHOW = true;

    private static final WebOrApp DEFAULT_SYS_CODE = WebOrApp.WEB;
    private static final WebOrApp UPDATED_SYS_CODE = WebOrApp.APP;

    private static final Status DEFAULT_STATUS = Status.DELETE;
    private static final Status UPDATED_STATUS = Status.NORMAL;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuMockMvc;

    private Menu menu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menu createEntity(EntityManager em) {
        Menu menu = new Menu()
            .menuCode(DEFAULT_MENU_CODE)
            .parentId(DEFAULT_PARENT_ID)
            .parentIds(DEFAULT_PARENT_IDS)
            .treeSort(DEFAULT_TREE_SORT)
            .treeSorts(DEFAULT_TREE_SORTS)
            .treeLeaf(DEFAULT_TREE_LEAF)
            .treeLevel(DEFAULT_TREE_LEVEL)
            .treeNames(DEFAULT_TREE_NAMES)
            .menuName(DEFAULT_MENU_NAME)
            .menuType(DEFAULT_MENU_TYPE)
            .menuHref(DEFAULT_MENU_HREF)
            .menuIcon(DEFAULT_MENU_ICON)
            .menuTitle(DEFAULT_MENU_TITLE)
            .permission(DEFAULT_PERMISSION)
            .menuSort(DEFAULT_MENU_SORT)
            .isShow(DEFAULT_IS_SHOW)
            .sysCode(DEFAULT_SYS_CODE)
            .status(DEFAULT_STATUS)
            .remarks(DEFAULT_REMARKS);
        return menu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menu createUpdatedEntity(EntityManager em) {
        Menu menu = new Menu()
            .menuCode(UPDATED_MENU_CODE)
            .parentId(UPDATED_PARENT_ID)
            .parentIds(UPDATED_PARENT_IDS)
            .treeSort(UPDATED_TREE_SORT)
            .treeSorts(UPDATED_TREE_SORTS)
            .treeLeaf(UPDATED_TREE_LEAF)
            .treeLevel(UPDATED_TREE_LEVEL)
            .treeNames(UPDATED_TREE_NAMES)
            .menuName(UPDATED_MENU_NAME)
            .menuType(UPDATED_MENU_TYPE)
            .menuHref(UPDATED_MENU_HREF)
            .menuIcon(UPDATED_MENU_ICON)
            .menuTitle(UPDATED_MENU_TITLE)
            .permission(UPDATED_PERMISSION)
            .menuSort(UPDATED_MENU_SORT)
            .isShow(UPDATED_IS_SHOW)
            .sysCode(UPDATED_SYS_CODE)
            .status(UPDATED_STATUS)
            .remarks(UPDATED_REMARKS);
        return menu;
    }

    @BeforeEach
    public void initTest() {
        menu = createEntity(em);
    }

    @Test
    @Transactional
    void createMenu() throws Exception {
        int databaseSizeBeforeCreate = menuRepository.findAll().size();
        // Create the Menu
        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isCreated());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeCreate + 1);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getMenuCode()).isEqualTo(DEFAULT_MENU_CODE);
        assertThat(testMenu.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testMenu.getParentIds()).isEqualTo(DEFAULT_PARENT_IDS);
        assertThat(testMenu.getTreeSort()).isEqualTo(DEFAULT_TREE_SORT);
        assertThat(testMenu.getTreeSorts()).isEqualTo(DEFAULT_TREE_SORTS);
        assertThat(testMenu.getTreeLeaf()).isEqualTo(DEFAULT_TREE_LEAF);
        assertThat(testMenu.getTreeLevel()).isEqualTo(DEFAULT_TREE_LEVEL);
        assertThat(testMenu.getTreeNames()).isEqualTo(DEFAULT_TREE_NAMES);
        assertThat(testMenu.getMenuName()).isEqualTo(DEFAULT_MENU_NAME);
        assertThat(testMenu.getMenuType()).isEqualTo(DEFAULT_MENU_TYPE);
        assertThat(testMenu.getMenuHref()).isEqualTo(DEFAULT_MENU_HREF);
        assertThat(testMenu.getMenuIcon()).isEqualTo(DEFAULT_MENU_ICON);
        assertThat(testMenu.getMenuTitle()).isEqualTo(DEFAULT_MENU_TITLE);
        assertThat(testMenu.getPermission()).isEqualTo(DEFAULT_PERMISSION);
        assertThat(testMenu.getMenuSort()).isEqualTo(DEFAULT_MENU_SORT);
        assertThat(testMenu.getIsShow()).isEqualTo(DEFAULT_IS_SHOW);
        assertThat(testMenu.getSysCode()).isEqualTo(DEFAULT_SYS_CODE);
        assertThat(testMenu.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMenu.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void createMenuWithExistingId() throws Exception {
        // Create the Menu with an existing ID
        menu.setId(1L);

        int databaseSizeBeforeCreate = menuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTreeLeafIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setTreeLeaf(null);

        // Create the Menu, which fails.

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTreeLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setTreeLevel(null);

        // Create the Menu, which fails.

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMenuNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setMenuName(null);

        // Create the Menu, which fails.

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMenuTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setMenuType(null);

        // Create the Menu, which fails.

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMenuSortIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setMenuSort(null);

        // Create the Menu, which fails.

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsShowIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setIsShow(null);

        // Create the Menu, which fails.

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuRepository.findAll().size();
        // set the field null
        menu.setStatus(null);

        // Create the Menu, which fails.

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isBadRequest());

        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMenus() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get all the menuList
        restMenuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menu.getId().intValue())))
            .andExpect(jsonPath("$.[*].menuCode").value(hasItem(DEFAULT_MENU_CODE)))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID)))
            .andExpect(jsonPath("$.[*].parentIds").value(hasItem(DEFAULT_PARENT_IDS)))
            .andExpect(jsonPath("$.[*].treeSort").value(hasItem(DEFAULT_TREE_SORT)))
            .andExpect(jsonPath("$.[*].treeSorts").value(hasItem(DEFAULT_TREE_SORTS)))
            .andExpect(jsonPath("$.[*].treeLeaf").value(hasItem(DEFAULT_TREE_LEAF.booleanValue())))
            .andExpect(jsonPath("$.[*].treeLevel").value(hasItem(DEFAULT_TREE_LEVEL)))
            .andExpect(jsonPath("$.[*].treeNames").value(hasItem(DEFAULT_TREE_NAMES)))
            .andExpect(jsonPath("$.[*].menuName").value(hasItem(DEFAULT_MENU_NAME)))
            .andExpect(jsonPath("$.[*].menuType").value(hasItem(DEFAULT_MENU_TYPE.toString())))
            .andExpect(jsonPath("$.[*].menuHref").value(hasItem(DEFAULT_MENU_HREF)))
            .andExpect(jsonPath("$.[*].menuIcon").value(hasItem(DEFAULT_MENU_ICON)))
            .andExpect(jsonPath("$.[*].menuTitle").value(hasItem(DEFAULT_MENU_TITLE)))
            .andExpect(jsonPath("$.[*].permission").value(hasItem(DEFAULT_PERMISSION)))
            .andExpect(jsonPath("$.[*].menuSort").value(hasItem(DEFAULT_MENU_SORT)))
            .andExpect(jsonPath("$.[*].isShow").value(hasItem(DEFAULT_IS_SHOW.booleanValue())))
            .andExpect(jsonPath("$.[*].sysCode").value(hasItem(DEFAULT_SYS_CODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @Test
    @Transactional
    void getMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        // Get the menu
        restMenuMockMvc
            .perform(get(ENTITY_API_URL_ID, menu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menu.getId().intValue()))
            .andExpect(jsonPath("$.menuCode").value(DEFAULT_MENU_CODE))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID))
            .andExpect(jsonPath("$.parentIds").value(DEFAULT_PARENT_IDS))
            .andExpect(jsonPath("$.treeSort").value(DEFAULT_TREE_SORT))
            .andExpect(jsonPath("$.treeSorts").value(DEFAULT_TREE_SORTS))
            .andExpect(jsonPath("$.treeLeaf").value(DEFAULT_TREE_LEAF.booleanValue()))
            .andExpect(jsonPath("$.treeLevel").value(DEFAULT_TREE_LEVEL))
            .andExpect(jsonPath("$.treeNames").value(DEFAULT_TREE_NAMES))
            .andExpect(jsonPath("$.menuName").value(DEFAULT_MENU_NAME))
            .andExpect(jsonPath("$.menuType").value(DEFAULT_MENU_TYPE.toString()))
            .andExpect(jsonPath("$.menuHref").value(DEFAULT_MENU_HREF))
            .andExpect(jsonPath("$.menuIcon").value(DEFAULT_MENU_ICON))
            .andExpect(jsonPath("$.menuTitle").value(DEFAULT_MENU_TITLE))
            .andExpect(jsonPath("$.permission").value(DEFAULT_PERMISSION))
            .andExpect(jsonPath("$.menuSort").value(DEFAULT_MENU_SORT))
            .andExpect(jsonPath("$.isShow").value(DEFAULT_IS_SHOW.booleanValue()))
            .andExpect(jsonPath("$.sysCode").value(DEFAULT_SYS_CODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getNonExistingMenu() throws Exception {
        // Get the menu
        restMenuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Update the menu
        Menu updatedMenu = menuRepository.findById(menu.getId()).get();
        // Disconnect from session so that the updates on updatedMenu are not directly saved in db
        em.detach(updatedMenu);
        updatedMenu
            .menuCode(UPDATED_MENU_CODE)
            .parentId(UPDATED_PARENT_ID)
            .parentIds(UPDATED_PARENT_IDS)
            .treeSort(UPDATED_TREE_SORT)
            .treeSorts(UPDATED_TREE_SORTS)
            .treeLeaf(UPDATED_TREE_LEAF)
            .treeLevel(UPDATED_TREE_LEVEL)
            .treeNames(UPDATED_TREE_NAMES)
            .menuName(UPDATED_MENU_NAME)
            .menuType(UPDATED_MENU_TYPE)
            .menuHref(UPDATED_MENU_HREF)
            .menuIcon(UPDATED_MENU_ICON)
            .menuTitle(UPDATED_MENU_TITLE)
            .permission(UPDATED_PERMISSION)
            .menuSort(UPDATED_MENU_SORT)
            .isShow(UPDATED_IS_SHOW)
            .sysCode(UPDATED_SYS_CODE)
            .status(UPDATED_STATUS)
            .remarks(UPDATED_REMARKS);

        restMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMenu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMenu))
            )
            .andExpect(status().isOk());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getMenuCode()).isEqualTo(UPDATED_MENU_CODE);
        assertThat(testMenu.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testMenu.getParentIds()).isEqualTo(UPDATED_PARENT_IDS);
        assertThat(testMenu.getTreeSort()).isEqualTo(UPDATED_TREE_SORT);
        assertThat(testMenu.getTreeSorts()).isEqualTo(UPDATED_TREE_SORTS);
        assertThat(testMenu.getTreeLeaf()).isEqualTo(UPDATED_TREE_LEAF);
        assertThat(testMenu.getTreeLevel()).isEqualTo(UPDATED_TREE_LEVEL);
        assertThat(testMenu.getTreeNames()).isEqualTo(UPDATED_TREE_NAMES);
        assertThat(testMenu.getMenuName()).isEqualTo(UPDATED_MENU_NAME);
        assertThat(testMenu.getMenuType()).isEqualTo(UPDATED_MENU_TYPE);
        assertThat(testMenu.getMenuHref()).isEqualTo(UPDATED_MENU_HREF);
        assertThat(testMenu.getMenuIcon()).isEqualTo(UPDATED_MENU_ICON);
        assertThat(testMenu.getMenuTitle()).isEqualTo(UPDATED_MENU_TITLE);
        assertThat(testMenu.getPermission()).isEqualTo(UPDATED_PERMISSION);
        assertThat(testMenu.getMenuSort()).isEqualTo(UPDATED_MENU_SORT);
        assertThat(testMenu.getIsShow()).isEqualTo(UPDATED_IS_SHOW);
        assertThat(testMenu.getSysCode()).isEqualTo(UPDATED_SYS_CODE);
        assertThat(testMenu.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMenu.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void putNonExistingMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuWithPatch() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Update the menu using partial update
        Menu partialUpdatedMenu = new Menu();
        partialUpdatedMenu.setId(menu.getId());

        partialUpdatedMenu
            .treeSort(UPDATED_TREE_SORT)
            .menuName(UPDATED_MENU_NAME)
            .menuType(UPDATED_MENU_TYPE)
            .menuIcon(UPDATED_MENU_ICON)
            .menuTitle(UPDATED_MENU_TITLE)
            .permission(UPDATED_PERMISSION)
            .remarks(UPDATED_REMARKS);

        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenu))
            )
            .andExpect(status().isOk());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getMenuCode()).isEqualTo(DEFAULT_MENU_CODE);
        assertThat(testMenu.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testMenu.getParentIds()).isEqualTo(DEFAULT_PARENT_IDS);
        assertThat(testMenu.getTreeSort()).isEqualTo(UPDATED_TREE_SORT);
        assertThat(testMenu.getTreeSorts()).isEqualTo(DEFAULT_TREE_SORTS);
        assertThat(testMenu.getTreeLeaf()).isEqualTo(DEFAULT_TREE_LEAF);
        assertThat(testMenu.getTreeLevel()).isEqualTo(DEFAULT_TREE_LEVEL);
        assertThat(testMenu.getTreeNames()).isEqualTo(DEFAULT_TREE_NAMES);
        assertThat(testMenu.getMenuName()).isEqualTo(UPDATED_MENU_NAME);
        assertThat(testMenu.getMenuType()).isEqualTo(UPDATED_MENU_TYPE);
        assertThat(testMenu.getMenuHref()).isEqualTo(DEFAULT_MENU_HREF);
        assertThat(testMenu.getMenuIcon()).isEqualTo(UPDATED_MENU_ICON);
        assertThat(testMenu.getMenuTitle()).isEqualTo(UPDATED_MENU_TITLE);
        assertThat(testMenu.getPermission()).isEqualTo(UPDATED_PERMISSION);
        assertThat(testMenu.getMenuSort()).isEqualTo(DEFAULT_MENU_SORT);
        assertThat(testMenu.getIsShow()).isEqualTo(DEFAULT_IS_SHOW);
        assertThat(testMenu.getSysCode()).isEqualTo(DEFAULT_SYS_CODE);
        assertThat(testMenu.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMenu.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdateMenuWithPatch() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeUpdate = menuRepository.findAll().size();

        // Update the menu using partial update
        Menu partialUpdatedMenu = new Menu();
        partialUpdatedMenu.setId(menu.getId());

        partialUpdatedMenu
            .menuCode(UPDATED_MENU_CODE)
            .parentId(UPDATED_PARENT_ID)
            .parentIds(UPDATED_PARENT_IDS)
            .treeSort(UPDATED_TREE_SORT)
            .treeSorts(UPDATED_TREE_SORTS)
            .treeLeaf(UPDATED_TREE_LEAF)
            .treeLevel(UPDATED_TREE_LEVEL)
            .treeNames(UPDATED_TREE_NAMES)
            .menuName(UPDATED_MENU_NAME)
            .menuType(UPDATED_MENU_TYPE)
            .menuHref(UPDATED_MENU_HREF)
            .menuIcon(UPDATED_MENU_ICON)
            .menuTitle(UPDATED_MENU_TITLE)
            .permission(UPDATED_PERMISSION)
            .menuSort(UPDATED_MENU_SORT)
            .isShow(UPDATED_IS_SHOW)
            .sysCode(UPDATED_SYS_CODE)
            .status(UPDATED_STATUS)
            .remarks(UPDATED_REMARKS);

        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenu))
            )
            .andExpect(status().isOk());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getMenuCode()).isEqualTo(UPDATED_MENU_CODE);
        assertThat(testMenu.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testMenu.getParentIds()).isEqualTo(UPDATED_PARENT_IDS);
        assertThat(testMenu.getTreeSort()).isEqualTo(UPDATED_TREE_SORT);
        assertThat(testMenu.getTreeSorts()).isEqualTo(UPDATED_TREE_SORTS);
        assertThat(testMenu.getTreeLeaf()).isEqualTo(UPDATED_TREE_LEAF);
        assertThat(testMenu.getTreeLevel()).isEqualTo(UPDATED_TREE_LEVEL);
        assertThat(testMenu.getTreeNames()).isEqualTo(UPDATED_TREE_NAMES);
        assertThat(testMenu.getMenuName()).isEqualTo(UPDATED_MENU_NAME);
        assertThat(testMenu.getMenuType()).isEqualTo(UPDATED_MENU_TYPE);
        assertThat(testMenu.getMenuHref()).isEqualTo(UPDATED_MENU_HREF);
        assertThat(testMenu.getMenuIcon()).isEqualTo(UPDATED_MENU_ICON);
        assertThat(testMenu.getMenuTitle()).isEqualTo(UPDATED_MENU_TITLE);
        assertThat(testMenu.getPermission()).isEqualTo(UPDATED_PERMISSION);
        assertThat(testMenu.getMenuSort()).isEqualTo(UPDATED_MENU_SORT);
        assertThat(testMenu.getIsShow()).isEqualTo(UPDATED_IS_SHOW);
        assertThat(testMenu.getSysCode()).isEqualTo(UPDATED_SYS_CODE);
        assertThat(testMenu.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMenu.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().size();
        menu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(menu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenu() throws Exception {
        // Initialize the database
        menuRepository.saveAndFlush(menu);

        int databaseSizeBeforeDelete = menuRepository.findAll().size();

        // Delete the menu
        restMenuMockMvc
            .perform(delete(ENTITY_API_URL_ID, menu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Menu> menuList = menuRepository.findAll();
        assertThat(menuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
