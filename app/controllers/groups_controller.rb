class GroupsController < ApplicationController
  before_action :set_group, only: %i[ show update destroy ]

  # GET /groups
  def index
    paginated_groups = Paginator.new(Group).paginate(page: params[:page], per: params[:per])

    render_json :success, data: paginated_groups
  end

  # GET /groups/1
  def show
    render_json :success, data: @group
  end

  # POST /groups
  def create
    @group = Group.new(group_params)

    if @group.save
      render_json :success, data: @group, status_code: :created, location: @group
    else
      render_json :fail, status_code: :unprocessable_entity, field: @group.errors
    end
  end

  # PATCH/PUT /groups/1
  def update
    if @group.update(group_params)
      render_json :success, data: @group
    else
      render_json :fail, status_code: :unprocessable_entity, field: @group.errors
    end
  end

  # DELETE /groups/1
  def destroy
    @group.destroy!
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_group
      @group = Group.find(params[:id])
    end

    # Only allow a list of trusted parameters through.
    def group_params
      params.require(:group).permit(:name)
    end
end
