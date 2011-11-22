$.jgrid.no_legacy_api = true;

$.jgrid.useJSON = true;

$(function(){
    $("#listingGrid").jqGrid({
        url: '/userRoles/grid',
        datatype: 'json',
        mtype: 'GET',
        jsonReader: {
            root: "rows"
        },
        colModel: [ { name: 'Name', index: 'name', hidden: false, sortable: true  }, 
{ name: 'User', index: 'user', hidden: false, sortable: true  }, 
           {
            name: '',
            index: 'show',
            formatter: showlink,
            formatoptions: {
                baseLinkUrl: '/userRoles/__id__',
                linkText: 'Details'
            }
        }, {
            name: '',
            index: 'edit',
            formatter: showlink,
            formatoptions: {
                baseLinkUrl: '/userRoles/__id__/edit',
                linkText: 'Edit'
            }
        }, {
            name: '',
            index: 'delete',
            formatter: showlink,
            formatoptions: {
                baseLinkUrl: '/userRoles/__id__/delete',
                linkText: 'Delete'
            }
        }],
        pager: '#pager',
        rowNum: 10,
        rowList: [10, 20, 30],
        viewrecords: true,
        gridview: true
    });

$("#listingGrid").jqGrid('navGrid', '#pager', {
    edit: false,
    add: false,
    del: false
}, {}, {}, {}, {
    multipleSearch: true,
    multipleGroup: false,
    showQuery: true
});

});