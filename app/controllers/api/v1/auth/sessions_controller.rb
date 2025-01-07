class Api::V1::Auth::SessionsController < Devise::SessionsController
  include ApiResponse
  include ExceptionHandler
  respond_to :json

  # TODO: 로그아웃 시 access_token jti, refresh_token 처리 필요
  # 로그아웃 시 어떤 refresh_tokens의 revoked_at을 처리해야 할지(모든 refresh_token revoke?)
  # access_token jti도 처리 필요
  # 현재는 사실 상 로그인 후에도 access_token, refresh_token 모두 사용 가능

  # TODO: 카카오 연동 계정의 로그아웃 시 처리 필요
  # 카카오 인증 서버 로그아웃 API 같이 호출 필요

  # TODO: 카카오 인증 서버 access_token, refresh_token 갱신 로직 필요
  # 별도 서비스 필요, 일단 여기에 TODO로 작성해둠

  def create
    super do |user|
      if user.persisted?
        device_info = request.user_agent
        access_token, refresh_token = Auth::TokenService.issue_tokens(user, device_info)

        return render_json :success, data: { token: { access_token: access_token, refresh_token: refresh_token }, user: user.as_json(only: [:id, :email]) }
      end
    end
  end

  def refresh
    # TODO: 토큰 갱신 시 access_token jti 처리 필요
    # 토큰 갱신 시 요청된 refresh_token의 revoked_at은 처리되는데 access_token은 처리하지 않아서 계속해서 유효함
    # access_token jit 처리 필요

    params.require(:refresh_token)

    refresh_token = params[:refresh_token]
    device_info = request.user_agent

    user, access_token, new_refresh_token = Auth::TokenService.refresh_tokens(refresh_token, device_info)

    render_json :success, data: { token: { access_token: access_token, refresh_token: new_refresh_token }, user: user.as_json(only: [:id, :email]) }
  end

  private

  def respond_to_on_destroy
    render json: { message: "Logged out successfully" }
  end
end
