package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.ui.model.GridPreset_;
import com.flexicore.ui.model.TableColumn_;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.wizzdi.flexicore.security.response.PaginationResponse;

import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.model.TableColumn;
import com.flexicore.ui.request.TableColumnCreate;
import com.flexicore.ui.request.TableColumnFiltering;
import com.flexicore.ui.request.TableColumnUpdate;
import com.flexicore.ui.service.TableColumnService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




/**
 * Created by Asaf on 04/06/2017.
 */


@OperationsInside
@RestController
@RequestMapping("plugins/TableColumns")

@Tag(name = "TableColumns")
@Extension
@Component
public class TableColumnController implements Plugin {

	
	@Autowired
	private TableColumnService service;


	

	@Operation(summary = "getAllTableColumns", description = "List all Ui Fields")

	@PostMapping("getAllTableColumns")
	public PaginationResponse<TableColumn> getAllTableColumns(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody 
			TableColumnFiltering tableColumnFiltering,
			@RequestAttribute SecurityContextBase securityContext) {
		service.validate(tableColumnFiltering, securityContext);
		return service
				.getAllTableColumns(tableColumnFiltering, securityContext);

	}

	

	@Operation(summary = "updateTableColumn", description = "Updates Ui Field")
	@PutMapping("updateTableColumn")
	public TableColumn updateTableColumn(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody 
			TableColumnUpdate tableColumnUpdate,
			@RequestAttribute SecurityContextBase securityContext) {
		TableColumn tableColumn = tableColumnUpdate.getId() != null ? service
				.getByIdOrNull(tableColumnUpdate.getId(), TableColumn.class,
						TableColumn_.security, securityContext) : null;
		if (tableColumn == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no ui field with id  "
					+ tableColumnUpdate.getId());
		}
		tableColumnUpdate.setTableColumn(tableColumn);

		return service.TableColumnUpdate(tableColumnUpdate, securityContext);

	}

	

	@Operation(summary = "createTableColumn", description = "Creates Ui Field ")
	@PostMapping("createTableColumn")
	public TableColumn createTableColumn(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody
			TableColumnCreate createTableColumn,
			@RequestAttribute SecurityContextBase securityContext) {
		GridPreset preset = createTableColumn.getPresetId() != null ? service.getByIdOrNull(createTableColumn.getPresetId(), GridPreset.class, GridPreset_.security, securityContext) : null;
		if (preset == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no GridPreset with id " + createTableColumn.getPresetId());
		}
		createTableColumn.setPreset(preset);
		service.validate(createTableColumn, securityContext);
		return service.createTableColumn(createTableColumn, securityContext);

	}

}
