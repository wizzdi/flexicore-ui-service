package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
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

import javax.ws.rs.*;
import javax.ws.rs.core.Context;

/**
 * Created by Asaf on 04/06/2017.
 */

@PluginInfo(version = 1)
@OperationsInside
@ProtectedREST
@Path("plugins/TableColumns")
@OpenAPIDefinition(tags = {@Tag(name = "TableColumns", description = "TableColumns Services")})
@Tag(name = "TableColumns")
@Extension
@Component
public class TableColumnRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private TableColumnService service;


	@POST
	@Produces("application/json")
	@Operation(summary = "getAllTableColumns", description = "List all Ui Fields")

	@Path("getAllTableColumns")
	public PaginationResponse<TableColumn> getAllTableColumns(
			@HeaderParam("authenticationKey") String authenticationKey,
			TableColumnFiltering tableColumnFiltering,
			@Context SecurityContext securityContext) {
		service.validate(tableColumnFiltering, securityContext);
		return service
				.getAllTableColumns(tableColumnFiltering, securityContext);

	}

	@PUT
	@Produces("application/json")
	@Operation(summary = "updateTableColumn", description = "Updates Ui Field")
	@Path("updateTableColumn")
	public TableColumn updateTableColumn(
			@HeaderParam("authenticationKey") String authenticationKey,
			TableColumnUpdate tableColumnUpdate,
			@Context SecurityContext securityContext) {
		TableColumn tableColumn = tableColumnUpdate.getId() != null ? service
				.getByIdOrNull(tableColumnUpdate.getId(), TableColumn.class,
						null, securityContext) : null;
		if (tableColumn == null) {
			throw new BadRequestException("no ui field with id  "
					+ tableColumnUpdate.getId());
		}
		tableColumnUpdate.setTableColumn(tableColumn);

		return service.TableColumnUpdate(tableColumnUpdate, securityContext);

	}

	@POST
	@Produces("application/json")
	@Operation(summary = "createTableColumn", description = "Creates Ui Field ")
	@Path("createTableColumn")
	public TableColumn createTableColumn(
			@HeaderParam("authenticationKey") String authenticationKey,
			TableColumnCreate createTableColumn,
			@Context SecurityContext securityContext) {
		GridPreset preset = createTableColumn.getPresetId() != null ? service
				.getByIdOrNull(createTableColumn.getPresetId(),
						GridPreset.class, null, securityContext) : null;
		if (preset == null) {
			throw new BadRequestException("no GridPreset with id "
					+ createTableColumn.getPresetId());
		}
		createTableColumn.setPreset(preset);
		service.validate(createTableColumn, securityContext);
		return service.createTableColumn(createTableColumn, securityContext);

	}

}
